package com.example.RESTAPIDB.Services;

import com.example.RESTAPIDB.Repository.ReposicionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReposicionService {
    @Autowired
    private ReposicionRepo reposicionRepo;
}
