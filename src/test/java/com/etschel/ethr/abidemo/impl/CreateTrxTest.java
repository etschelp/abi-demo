package com.etschel.ethr.abidemo.impl;

import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.AbiTypes;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.core.methods.request.Transaction;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.List;

public class CreateTrxTest {

    @Test
    void testCreateTrx() {
        Function function = new Function(
                "functionName",
                List.of(),
                List.of());

        String encodedFunction = FunctionEncoder.encode(function);
        RawTransaction transaction = RawTransaction.createTransaction(BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, "to", encodedFunction);
        byte[] encode = TransactionEncoder.encode(transaction);
        System.out.println(Hex.encodeHexString(encode));
    }
}
