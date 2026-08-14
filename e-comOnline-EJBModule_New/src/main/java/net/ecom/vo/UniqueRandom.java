package net.ecom.vo;

import java.util.ArrayList;
import java.util.Vector;
import java.util.List;

public class UniqueRandom {

    public UniqueRandom() {
    }
    private List seed1 = null;
    private long next1;
    private int length1;

    public UniqueRandom(String[] parts, int length) {
        length1 = length;
        seed1 = new Vector();
        List all = new Vector();
        for (int i = 0; i < parts.length; i++) {

            all.add(parts[i]);
        }
        while (all.size() > 0) {
            int p = (int) (((double) all.size()) * Math.random());
            seed1.add(all.get(p));
            all.remove(p);
        }
        int p = 1;
        next1 = p;
        for (int i = 0; i < length - 1; i++) {
            p = p * 2;
            next1 = next1 + p;
        }
    }

    public String nextUnique() {
        StringBuffer sb = new StringBuffer();
        long p = 1;
        next1 = nextNumberWithSameNumberOfBits(next1);
        for (int i = 0; i < seed1.size(); i++) {
            if ((next1 & p) > 0) {
                sb.append(seed1.get(i));
            }
            p = p * 2;

        }

        return sb.toString();
    }

    private long nextNumberWithSameNumberOfBits(long a) {
        long length1 = a & -a;
        long r = a + length1;
        a = (((r ^ a) >>> 2) / length1) | r;
        return a;
    }
    
}



    

