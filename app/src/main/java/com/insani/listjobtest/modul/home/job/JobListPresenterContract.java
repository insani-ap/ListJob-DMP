package com.insani.listjobtest.modul.home.job;

public interface JobListPresenterContract {
    void loadList(Integer page);
    void findList(String description, boolean isFulltime, String location);
    void openDetail(String id);
}
