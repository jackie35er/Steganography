import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class BitBuffer implements Serializable {
    private List<Boolean> bits;

    public BitBuffer(){
        bits = new ArrayList<>();
    }

    public int size() {
        return bits.size();
    }

    public BitBuffer(byte b){
        bits = new ArrayList<>(8);
        for(int i = 0; i < 8;i++){
            bits.add(((b >> i) & 1) == 1);
        }
        Collections.reverse(bits);

    }

    public BitBuffer(byte[] bytes){
        if(bytes == null){
            throw new IllegalArgumentException("bytes is null");
        }

        bits = new ArrayList<>(bytes.length * 8);
        for (byte b  : bytes) {
            ArrayList<Boolean> temp = new ArrayList<>(8);
            for (int j = 0; j < 8; j++) {
                temp.add((((b >> j) & 1) == 1));
            }
            Collections.reverse(temp);
            bits.addAll(temp);
        }
    }

    public BitBuffer(int number){
        byte[] bytes = {
                (byte) ((number >> 24) & 0xFF),
                (byte) ((number >> 16) & 0xFF),
                (byte) ((number >>  8) & 0xFF),
                (byte) ((number) & 0xFF)
        };
        bits = new ArrayList<>(bytes.length * 8);
        for (byte b  : bytes) {
            ArrayList<Boolean> temp = new ArrayList<>(8);
            for (int j = 0; j < 8; j++) {
                temp.add((((b >> j) & 1) == 1));
            }
            Collections.reverse(temp);
            bits.addAll(temp);
        }
    }
    public BitBuffer(double number){
        long data = Double.doubleToRawLongBits(number);
        byte[] bytes = {
                (byte) ((data >> 56) & 0xff),
                (byte) ((data >> 48) & 0xff),
                (byte) ((data >> 40) & 0xff),
                (byte) ((data >> 32) & 0xff),
                (byte) ((data >> 24) & 0xff),
                (byte) ((data >> 16) & 0xff),
                (byte) ((data >>  8) & 0xff),
                (byte) ((data) & 0xff),
        };
        bits = new ArrayList<>(bytes.length * 8);
        for (byte b  : bytes) {
            ArrayList<Boolean> temp = new ArrayList<>(8);
            for (int j = 0; j < 8; j++) {
                temp.add((((b >> j) & 1) == 1));
            }
            Collections.reverse(temp);
            bits.addAll(temp);
        }
    }

    public BitBuffer(long number){
        byte[] bytes = {
                (byte) ((number >> 56) & 0xff),
                (byte) ((number >> 48) & 0xff),
                (byte) ((number >> 40) & 0xff),
                (byte) ((number >> 32) & 0xff),
                (byte) ((number >> 24) & 0xff),
                (byte) ((number >> 16) & 0xff),
                (byte) ((number >>  8) & 0xff),
                (byte) ((number) & 0xff),
        };
        bits = new ArrayList<>(bytes.length * 8);
        for (byte b  : bytes) {
            ArrayList<Boolean> temp = new ArrayList<>(8);
            for (int j = 0; j < 8; j++) {
                temp.add((((b >> j) & 1) == 1));
            }
            Collections.reverse(temp);
            bits.addAll(temp);
        }
    }

    public BitBuffer(short number){
        byte[] bytes = {
                (byte) ((number >>  8) & 0xff),
                (byte) ((number) & 0xff),
        };
        bits = new ArrayList<>(bytes.length * 8);
        for (byte b  : bytes) {
            ArrayList<Boolean> temp = new ArrayList<>(8);
            for (int j = 0; j < 8; j++) {
                temp.add((((b >> j) & 1) == 1));
            }
            Collections.reverse(temp);
            bits.addAll(temp);
        }
    }

    public BitBuffer(char c){
        this((byte) c);
    }

    public BitBuffer(String str){
        this(str.getBytes(StandardCharsets.UTF_8));
    }

    public BitBuffer(String str, Charset charset){
        this(str.getBytes(charset));
    }

    public void set(int index,boolean b){
        if(index >= bits.size() || index < 0){
            throw new IndexOutOfBoundsException();
        }
        bits.set(index,b);
    }

    public void add(int index, boolean b){
        bits.add(index,b);
    }

    public void add(boolean b){
        bits.add(b);
    }

    public void add(byte[] bytes){
        if(bytes == null){
            throw new IllegalArgumentException("bytes is null");
        }

        bits = new ArrayList<>(bytes.length * 8);
        for (byte b  : bytes) {
            ArrayList<Boolean> temp = new ArrayList<>(8);
            for (int j = 0; j < 8; j++) {
                temp.add((((b >> j) & 1) == 1));
            }
            Collections.reverse(temp);
            bits.addAll(temp);
        }
    }

    public void add(int number){
        byte[] bytes = {
                (byte) ((number >> 24) & 0xFF),
                (byte) ((number >> 16) & 0xFF),
                (byte) ((number >>  8) & 0xFF),
                (byte) ((number) & 0xFF)
        };
        this.add(bytes);
    }

    public void add(short number){
        byte[] bytes = {
                (byte) ((number >>  8) & 0xFF),
                (byte) ((number) & 0xFF)
        };
        this.add(bytes);
    }

    public void add(double number){
        long data = Double.doubleToRawLongBits(number);
        byte[] bytes = {
                (byte) ((data >> 56) & 0xff),
                (byte) ((data >> 48) & 0xff),
                (byte) ((data >> 40) & 0xff),
                (byte) ((data >> 32) & 0xff),
                (byte) ((data >> 24) & 0xff),
                (byte) ((data >> 16) & 0xff),
                (byte) ((data >>  8) & 0xff),
                (byte) ((data) & 0xff),
        };

        this.add(bytes);
    }

    public void add(long number){
        byte[] bytes = {
                (byte) ((number >> 56) & 0xff),
                (byte) ((number >> 48) & 0xff),
                (byte) ((number >> 40) & 0xff),
                (byte) ((number >> 32) & 0xff),
                (byte) ((number >> 24) & 0xff),
                (byte) ((number >> 16) & 0xff),
                (byte) ((number >>  8) & 0xff),
                (byte) ((number) & 0xff),
        };
        this.add(bytes);
    }
    public void add(char c){
        this.add((byte) c);
    }


    public boolean get(int index){
        return bits.get(index);
    }

    public byte getByte(int index){
        if(index + 8 > bits.size()) {
            throw new IndexOutOfBoundsException();
        }
        byte b = 0;
        for(int i = 0; i < 8;i++){
            if(bits.get(i+index)){
                b += Math.pow(2,7-i);
            }
        }
        return b;
    }
    public Boolean[] get(int index, int from, int to){
       return (Boolean[]) Arrays.copyOfRange(bits.toArray(),from,to);
    }
}
