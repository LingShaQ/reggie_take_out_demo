package com.lingsha.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingsha.reggie.entity.AddressBook;
import com.lingsha.reggie.mapper.AddressBookMapper;
import com.lingsha.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @auther Ling.Sha
 * @date 2022/8/11 - 21:06
 */
@Service
@Slf4j
public class AddressBookImpl extends ServiceImpl<AddressBookMapper,AddressBook> implements AddressBookService {
}
