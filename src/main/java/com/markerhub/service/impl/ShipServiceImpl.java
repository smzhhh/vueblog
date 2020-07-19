package com.markerhub.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.markerhub.entity.Ship;
import com.markerhub.mapper.ShipMapper;
import com.markerhub.service.ShipService;
import org.springframework.stereotype.Service;

@Service
public class ShipServiceImpl extends ServiceImpl<ShipMapper, Ship> implements ShipService {
}
