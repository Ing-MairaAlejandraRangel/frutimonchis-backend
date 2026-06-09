package com.frutimonchis.backend.entity;

public enum EstadoCompra {
    PENDIENTE, // tiene saldo por pagar al proveedor
    PAGADA,    // saldo == 0
    ANULADA
}
