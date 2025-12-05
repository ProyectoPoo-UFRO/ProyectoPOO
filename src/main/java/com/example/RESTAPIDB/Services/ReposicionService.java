package com.example.RESTAPIDB.Services;

import com.example.RESTAPIDB.Repository.ReposicionRepo;
import org.springframework.stereotype.Service;

@Service
public class ReposicionService {

    private final ReposicionRepo reposicionRepo;

    public ReposicionService(ReposicionRepo reposicionRepo) {
        this.reposicionRepo = reposicionRepo;
    }

}