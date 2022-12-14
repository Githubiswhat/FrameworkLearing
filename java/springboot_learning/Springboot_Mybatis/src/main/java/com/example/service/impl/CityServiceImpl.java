package com.example.service.impl;

import com.example.dao.CityDao;
import com.example.domain.City;
import com.example.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CityServiceImpl implements CityService {

    private final CityDao cityDao;

    @Autowired
    public CityServiceImpl(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    public City findCityByName(String cityName) {
        return cityDao.findByName(cityName);
    }

}
