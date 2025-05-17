package com.lobo24.util;

import java.util.HashMap;
import java.util.Map;

public class RecoveryCodeStore {
    private static Map<String, String> userCodeMap = new HashMap<>();

    public static void guardarCodigo(String usuarioCorreo, String codigo) {
        userCodeMap.put(usuarioCorreo, codigo);
    }

    public static boolean validarCodigo(String usuarioCorreo, String codigoIngresado) {
        return codigoIngresado.equals(userCodeMap.get(usuarioCorreo));
    }

    public static void borrarCodigo(String usuarioCorreo) {
        userCodeMap.remove(usuarioCorreo);
    }
}
