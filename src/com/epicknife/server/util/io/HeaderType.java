package com.epicknife.server.util.io;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public enum HeaderType
{
    // Complex Types
    NULL, ARRAY_START, ARRAY_END, PAIR,

    // Primitives
    BYTE, BOOLEAN, SHORT, CHAR, INTEGER, FLOAT, LONG, DOUBLE, STRING,

    // Defines a custom DataHeader
    CUSTOM;

}