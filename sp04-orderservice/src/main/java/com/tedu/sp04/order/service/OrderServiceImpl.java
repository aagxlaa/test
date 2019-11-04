package com.tedu.sp04.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tedu.sp01.pojo.Item;
import com.tedu.sp01.pojo.Order;
import com.tedu.sp01.pojo.User;
import com.tedu.sp01.service.OrderService;
import com.tedu.sp04.order.feignclient.ItemFeignService;
import com.tedu.sp04.order.feignclient.UserFeignService;
import com.tedu.web.util.JsonResult;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private ItemFeignService itemServ;
	@Autowired
	private UserFeignService userServ;

	@Override
	public Order getOrder(String orderId) {
		//调用商品微服务,获取订单中的商品列表数据
		JsonResult<List<Item>> items = itemServ.getItems(orderId);
		
		//调用用户微服务,获取用户数据
		JsonResult<User> user = userServ.getUser(7);
		
		Order order = new Order();
		order.setId(orderId);
		order.setUser(user.getData());
		order.setItems(items.getData());
		return order;
	}

	@Override
	public void addOrder(Order order) {
		//减少商品的库存
		itemServ.decreaseNumber(order.getItems());
		
		//增加用户的积分
		userServ.addScore(order.getUser().getId(), 100);
		
		log.info("保存订单: "+order);
	}

}




