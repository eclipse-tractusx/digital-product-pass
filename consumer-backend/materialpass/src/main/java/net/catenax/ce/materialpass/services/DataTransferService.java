package net.catenax.ce.materialpass.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DataTransferService {
    @Autowired
    RestTemplate restTemplate;


}
