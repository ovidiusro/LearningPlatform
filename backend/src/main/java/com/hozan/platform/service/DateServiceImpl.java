package com.hozan.platform.service;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

@Service
public class DateServiceImpl implements DateService{
    @Override
    public DateTime now() {
        return new DateTime();
    }
}
