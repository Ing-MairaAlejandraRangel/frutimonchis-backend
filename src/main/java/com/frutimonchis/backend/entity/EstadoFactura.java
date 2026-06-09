package com.frutimonchis.backend.entity;

public enum EstadoFactura {
    PENDIENTE, // tiene saldo
    PAGADA,    // saldo == 0
    ANULADA
}
