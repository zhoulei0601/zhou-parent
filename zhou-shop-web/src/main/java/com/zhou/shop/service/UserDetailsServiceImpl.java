package com.zhou.shop.service;

import com.zhou.pojo.TbSeller;
import com.zhou.sellergoods.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 认证类
 * @author: zhoulei
 * @createTime: 2020-02-26 14:45
 **/
public class UserDetailsServiceImpl implements UserDetailsService {
    private SellerService sellerService;

    //配置注入
    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> authorities = new ArrayList<>();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_SELLER");
        authorities.add(grantedAuthority);
        TbSeller tbSeller = sellerService.findOne(username);
        if(tbSeller != null){
            //商家需审核通过
            if("1".equals(tbSeller.getStatus())){
                return new User(username,tbSeller.getPassword(),authorities);
            }
        }

        return null;
    }
}
