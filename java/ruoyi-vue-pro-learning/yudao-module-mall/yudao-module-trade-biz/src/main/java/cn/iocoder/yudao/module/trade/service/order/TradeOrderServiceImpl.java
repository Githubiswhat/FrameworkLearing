package cn.iocoder.yudao.module.trade.service.order;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.TerminalEnum;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.member.api.address.AddressApi;
import cn.iocoder.yudao.module.member.api.address.dto.AddressRespDTO;
import cn.iocoder.yudao.module.pay.api.order.PayOrderApi;
import cn.iocoder.yudao.module.pay.api.order.PayOrderInfoCreateReqDTO;
import cn.iocoder.yudao.module.product.api.sku.ProductSkuApi;
import cn.iocoder.yudao.module.product.api.sku.dto.ProductSkuRespDTO;
import cn.iocoder.yudao.module.product.api.sku.dto.ProductSkuUpdateStockReqDTO;
import cn.iocoder.yudao.module.product.api.spu.ProductSpuApi;
import cn.iocoder.yudao.module.product.api.spu.dto.ProductSpuRespDTO;
import cn.iocoder.yudao.module.product.enums.spu.ProductSpuStatusEnum;
import cn.iocoder.yudao.module.promotion.api.coupon.CouponApi;
import cn.iocoder.yudao.module.promotion.api.coupon.dto.CouponUseReqDTO;
import cn.iocoder.yudao.module.promotion.api.price.PriceApi;
import cn.iocoder.yudao.module.promotion.api.price.dto.PriceCalculateRespDTO;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.AppTradeOrderCreateReqVO;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.AppTradeOrderCreateReqVO.Item;
import cn.iocoder.yudao.module.trade.convert.order.TradeOrderConvert;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderItemDO;
import cn.iocoder.yudao.module.trade.dal.mysql.order.TradeOrderMapper;
import cn.iocoder.yudao.module.trade.dal.mysql.orderitem.TradeOrderItemMapper;
import cn.iocoder.yudao.module.trade.enums.ErrorCodeConstants;
import cn.iocoder.yudao.module.trade.enums.order.TradeOrderRefundStatusEnum;
import cn.iocoder.yudao.module.trade.enums.order.TradeOrderStatusEnum;
import cn.iocoder.yudao.module.trade.enums.order.TradeOrderTypeEnum;
import cn.iocoder.yudao.module.trade.framework.order.config.TradeOrderProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.*;
import static cn.iocoder.yudao.module.trade.enums.ErrorCodeConstants.ORDER_CREATE_SKU_NOT_SALE;
import static cn.iocoder.yudao.module.trade.enums.ErrorCodeConstants.ORDER_CREATE_SPU_NOT_FOUND;

/**
 * ???????????? Service ?????????
 *
 * @author LeeYan9
 * @since 2022-08-26
 */
@Service
public class TradeOrderServiceImpl implements TradeOrderService {

    @Resource
    private TradeOrderMapper tradeOrderMapper;
    @Resource
    private TradeOrderItemMapper tradeOrderItemMapper;

    @Resource
    private PriceApi priceApi;
    @Resource
    private ProductSkuApi productSkuApi;
    @Resource
    private ProductSpuApi productSpuApi;
    @Resource
    private PayOrderApi payOrderApi;
    @Resource
    private AddressApi addressApi;
    @Resource
    private CouponApi couponApi;

    @Resource
    private TradeOrderProperties tradeOrderProperties;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createTradeOrder(Long userId, String userIp, AppTradeOrderCreateReqVO createReqVO) {
        // ?????? SKU ??????????????????????????????
        List<ProductSkuRespDTO> skus = validateSkuSaleable(createReqVO.getItems());
        // ?????? SPU ?????????????????????
        List<ProductSpuRespDTO> spus = validateSpuSaleable(convertSet(skus, ProductSkuRespDTO::getSpuId));
        // ???????????????????????????
        AddressRespDTO address = validateAddress(userId, createReqVO.getAddressId());

        // ????????????
        PriceCalculateRespDTO priceResp = priceApi.calculatePrice(TradeOrderConvert.INSTANCE.convert(createReqVO, userId));

        // ?????? TradeOrderDO ??????
        TradeOrderDO tradeOrderDO = createTradeOrder(userId, userIp, createReqVO, priceResp.getOrder(), address);
        // ?????? TradeOrderItemDO ?????????
        List<TradeOrderItemDO> tradeOrderItems = createTradeOrderItems(tradeOrderDO, priceResp.getOrder().getItems(), skus);

        // ???????????????????????????
        afterCreateTradeOrder(userId, createReqVO, tradeOrderDO, tradeOrderItems, spus);
        // TODO @LeeYan9: ??????????????????, ???????????????????????????, ?????????????????????, ??????????????????!
        return tradeOrderDO.getId();
    }

    /**
     * ???????????? SKU ???????????????
     *
     * @param items ?????? SKU
     * @return ?????? SKU ??????
     */
    private List<ProductSkuRespDTO> validateSkuSaleable(List<Item> items) {
        List<ProductSkuRespDTO> skus = productSkuApi.getSkuList(convertSet(items, Item::getSkuId));
        // SKU ?????????
        if (items.size() != skus.size()) {
            throw exception(ErrorCodeConstants.ORDER_CREATE_SKU_NOT_FOUND);
        }
        // ?????????????????? or ????????????
        Map<Long, ProductSkuRespDTO> skuMap = convertMap(skus, ProductSkuRespDTO::getId);
        items.forEach(item -> {
            ProductSkuRespDTO sku = skuMap.get(item.getSkuId());
            // SKU ??????
            if (ObjectUtil.notEqual(CommonStatusEnum.ENABLE.getStatus(), sku.getStatus())) {
                throw exception(ORDER_CREATE_SKU_NOT_SALE);
            }
            // SKU ????????????
            if (item.getCount() > sku.getStock()) {
                throw exception(ErrorCodeConstants.ORDER_CREATE_SKU_STOCK_NOT_ENOUGH);
            }
        });
        return skus;
    }

    /**
     * ???????????? SPU ???????????????
     *
     * @param spuIds ?????? SPU ????????????
     * @return ?????? SPU ??????
     */
    private List<ProductSpuRespDTO> validateSpuSaleable(Set<Long> spuIds) {
        List<ProductSpuRespDTO> spus = productSpuApi.getSpuList(spuIds);
        // SPU ?????????
        if (spus.size() != spuIds.size()) {
            throw exception(ORDER_CREATE_SPU_NOT_FOUND);
        }
        // ??????????????????????????? SPU
        ProductSpuRespDTO spu = CollectionUtils.findFirst(spus,
                spuDTO -> ObjectUtil.notEqual(ProductSpuStatusEnum.ENABLE.getStatus(), spuDTO.getStatus()));
        if (spu != null) {
            throw exception(ErrorCodeConstants.ORDER_CREATE_SPU_NOT_SALE);
        }
        return spus;
    }

    /**
     * ??????????????????????????????
     *
     * @param userId ????????????
     * @param addressId ??????????????????
     * @return ????????????
     */
    private AddressRespDTO validateAddress(Long userId, Long addressId) {
        AddressRespDTO address = addressApi.getAddress(addressId, userId);
        if (Objects.isNull(address)) {
            throw exception(ErrorCodeConstants.ORDER_CREATE_ADDRESS_NOT_FOUND);
        }
        return address;
    }

    private TradeOrderDO createTradeOrder(Long userId, String clientIp, AppTradeOrderCreateReqVO createReqVO,
                                          PriceCalculateRespDTO.Order order, AddressRespDTO address) {
        TradeOrderDO tradeOrderDO = TradeOrderConvert.INSTANCE.convert(userId, clientIp, createReqVO, order, address);
        tradeOrderDO.setNo(IdUtil.getSnowflakeNextId() + ""); // TODO @LeeYan9: ?????????, ?????????????????????; ??????????????????????????????;
        tradeOrderDO.setStatus(TradeOrderStatusEnum.WAITING_PAYMENT.getStatus());
        tradeOrderDO.setType(TradeOrderTypeEnum.NORMAL.getType());
        tradeOrderDO.setRefundStatus(TradeOrderRefundStatusEnum.NONE.getStatus());
        tradeOrderDO.setProductCount(getSumValue(order.getItems(),  PriceCalculateRespDTO.OrderItem::getCount, Integer::sum));
        tradeOrderDO.setTerminal(TerminalEnum.H5.getTerminal()); // todo ?????????????
        tradeOrderDO.setAdjustPrice(0).setPayed(false); // ????????????
        tradeOrderDO.setDeliveryStatus(false); // ????????????
        tradeOrderDO.setRefundStatus(TradeOrderRefundStatusEnum.NONE.getStatus()).setRefundPrice(0); // ????????????
        tradeOrderMapper.insert(tradeOrderDO);
        return tradeOrderDO;
    }

    private List<TradeOrderItemDO> createTradeOrderItems(TradeOrderDO tradeOrderDO,
                                                         List<PriceCalculateRespDTO.OrderItem> orderItems, List<ProductSkuRespDTO> skus) {
        List<TradeOrderItemDO> tradeOrderItemDOs = TradeOrderConvert.INSTANCE.convertList(tradeOrderDO, orderItems, skus);
        tradeOrderItemMapper.insertBatch(tradeOrderItemDOs);
        return tradeOrderItemDOs;
    }

    /**
     * ??????????????????????????????????????????
     *
     * ???????????????????????????????????????????????????????????????????????????
     *
     * @param userId ????????????
     * @param createReqVO ??????????????????
     * @param tradeOrderDO ????????????
     */
    private void afterCreateTradeOrder(Long userId, AppTradeOrderCreateReqVO createReqVO,
                                       TradeOrderDO tradeOrderDO, List<TradeOrderItemDO> tradeOrderItemDOs,
                                       List<ProductSpuRespDTO> spus) {
        // ???????????????????????????
        productSkuApi.updateSkuStock(new ProductSkuUpdateStockReqDTO(TradeOrderConvert.INSTANCE.convertList(tradeOrderItemDOs)));

        // ????????????????????? TODO ??????????????????

        // ??????????????????????????? TODO ??????????????????

        // ???????????????????????????
        if (createReqVO.getCouponId() != null) {
            couponApi.useCoupon(new CouponUseReqDTO().setId(createReqVO.getCouponId()).setUserId(userId)
                    .setOrderId(tradeOrderDO.getId()));
        }

        // ???????????????
        createPayOrder(tradeOrderDO, tradeOrderItemDOs, spus);

        // ?????????????????? TODO ??????????????????
    }

    private void createPayOrder(TradeOrderDO tradeOrderDO, List<TradeOrderItemDO> tradeOrderItemDOs,
                                List<ProductSpuRespDTO> spus) {
        // ???????????????????????????????????????
        PayOrderInfoCreateReqDTO payOrderCreateReqDTO = TradeOrderConvert.INSTANCE.convert(
                tradeOrderDO, tradeOrderItemDOs, spus, tradeOrderProperties);
        Long payOrderId = payOrderApi.createPayOrder(payOrderCreateReqDTO);

        // ?????????????????????
        tradeOrderMapper.updateById(new TradeOrderDO().setId(tradeOrderDO.getId()).setPayOrderId(payOrderId));
    }

}
