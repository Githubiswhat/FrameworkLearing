package com.example.service.impl;

import com.example.domain.City;
import com.example.service.CityService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 城市业务逻辑实现类
 * <p>
 * Created by bysocket on 07/02/2017.
 */
@Service
public class CityServiceAnnotationImpl implements CityService {


    // 模拟数据库存储
    private Map<String, City> cityMap = new HashMap<String, City>();

    @Override
    public City findCityById(Long id) {
        return null;
    }

    public void saveCity(City city){
        // 模拟数据库插入操作
        cityMap.put(city.getCityName(), city);
    }

    @Override
    public Long updateCity(City city) {
        return null;
    }

    @Override
    public Long deleteCity(Long id) {
        return null;
    }

    @Cacheable(value = "baseCityInfo")
    public City getCityByName(String cityName){
        // 模拟数据库查询并返回
        return cityMap.get(cityName);
    }

    @CachePut(value = "baseCityInfo")
    public void updateCityDescription(String cityName, String description){
        City city = cityMap.get(cityName);
        city.setDescription(description);
        // 模拟更新数据库
        cityMap.put(cityName, city);
    }

}
