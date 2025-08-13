package com.uandmy.back.common.base;

public interface SoftDeletable {
    String getDeleteAt();
    void setDeleteAt(String deleteAt);
}