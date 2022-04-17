package ch.nostromo.tiffanys.dragonborn.engine.ai.callable;

public class TranspositionsTable {

    public static int VALUE_EXACT = 0;
    public static int VALUE_LOWER = 1;
    public static int VALUE_UPPER = 2;


    public int HASHSIZE = 2000000;
    public final int SLOTS = 3;

    public int[] hashtable = new int[HASHSIZE * SLOTS];

    public void reset() {
        hashtable = new int[HASHSIZE * SLOTS];
    }

    public void record(long zobrist, int depth, int flag, int eval) {
        int hashkey = (int) (zobrist % HASHSIZE) * SLOTS;

        if (eval >= 9000) eval += depth;
        else if (eval <= -9000) eval -= depth;

        hashtable[hashkey] = 0 | (eval + 0x1FFFF)
                | ((1) << 18) | (flag << 20)
                | (depth << 22);
        hashtable[hashkey + 1] = (int) (zobrist >> 32);
        hashtable[hashkey + 2] = (int) (zobrist & 0xFFFFFFFF);
    }


    public boolean entryExists(long zobrist) {
        int hashkey = (int) (zobrist % HASHSIZE) * SLOTS;

        return hashtable[hashkey + 1] == (int) (zobrist >> 32) && hashtable[hashkey + 2] == (int) (zobrist & 0xFFFFFFFF) &&
                hashtable[hashkey] != 0;
    }


    public int getEval(long zobrist) {
        int hashkey = (int) (zobrist % HASHSIZE) * SLOTS;

        if (hashtable[hashkey + 1] == (int) (zobrist >> 32) && hashtable[hashkey + 2] == (int) (zobrist & 0xFFFFFFFF)) {
            int result = ((hashtable[hashkey] & 0x3FFFF) - 0x1FFFF);
            if (result >= 9000) result -= getDepth(zobrist);
            else if (result <= -9000) result += getDepth(zobrist);
            return result;
        }

        return 0;
    }

    public int getFlag(long zobrist) {
        int hashkey = (int) (zobrist % HASHSIZE) * SLOTS;
        if (hashtable[hashkey + 1] == (int) (zobrist >> 32) && hashtable[hashkey + 2] == (int) (zobrist & 0xFFFFFFFF))
            return ((hashtable[hashkey] >> 20) & 3);

        return 0;
    }

    public int getMove(long zobrist) {
        int hashkey = (int) (zobrist % HASHSIZE) * SLOTS;
        if (hashtable[hashkey + 1] == (int) (zobrist >> 32) && hashtable[hashkey + 2] == (int) (zobrist & 0xFFFFFFFF))
            return hashtable[hashkey + 1];

        return 0;
    }

    public int getDepth(long zobrist) {
        int hashkey = (int) (zobrist % HASHSIZE) * SLOTS;
        if (hashtable[hashkey + 1] == (int) (zobrist >> 32) && hashtable[hashkey + 2] == (int) (zobrist & 0xFFFFFFFF))
            return (hashtable[hashkey] >> 22);

        return 0;
    }

}
