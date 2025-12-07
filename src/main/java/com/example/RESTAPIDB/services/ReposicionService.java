package com.example.RESTAPIDB.services;

import com.example.RESTAPIDB.repo.ReposicionRepo;
import org.springframework.stereotype.Service;

@Service
public class ReposicionService {

    private final ReposicionRepo reposicionRepo;

    public ReposicionService(ReposicionRepo reposicionRepo) {
        this.reposicionRepo = reposicionRepo;
    }

}