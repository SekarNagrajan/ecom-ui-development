/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.common;

import java.util.Random;

/**
 *
 * @author sasikumar.r
 */
public class RandomIdCreator {

    public static String generateRandomId() {
        String randomid1 = "", randomid2 = "", randomid3 = "", randomid = "";
        Random generator = null;
        try {
            generator = new Random();
            randomid1 = String.valueOf(generator.nextInt(10000) * generator.nextInt(10000));
            randomid2 = String.valueOf(generator.nextInt(10000) * generator.nextInt(10000));
            randomid3 = String.valueOf(generator.nextInt(10000) * generator.nextInt(10000));
            randomid = randomid1 + randomid2 + randomid3;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return randomid;
    }

}
