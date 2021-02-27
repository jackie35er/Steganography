import org.apache.commons.lang3.Validate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Steganography {

    public static byte[] hideData(byte[] data, byte[] textData){
        Validate.notNull(data,"data can't be null");
        Validate.notNull(textData,"text can't be null");
        Validate.isTrue(textData.length + 1 <= data.length / 4);//4 bytes needed to hide 1 byte. +1 is needed for '\0'

        byte[] output = data.clone();
        for(int i = 0; i < textData.length; i++){
            BitBuffer charBuffer = new BitBuffer(textData[i]);
            for(int j = 0; j < 4;j++){
                BitBuffer outputByte = new BitBuffer(output[i * 4 + j]);
                outputByte.set(6,charBuffer.get(j * 2));
                outputByte.set(7,charBuffer.get(j * 2 + 1));
                output[i * 4 + j] = outputByte.getByte(0);
            }
        }
        BitBuffer backslashNull = new BitBuffer(0);
        for(int j = 0; j < 4 ; j++){
            BitBuffer outputByte = new BitBuffer(output[textData.length * 4 + j]);
            outputByte.set(6,backslashNull.get(j * 2));
            outputByte.set(7,backslashNull.get(j * 2 + 1));
            output[textData.length * 4 + j] = outputByte.getByte(0);
        }
        return output;
    }

    public static byte[] getData(byte[] src){
        Validate.notNull(src,"src can't be null");

        List<Byte> out = new ArrayList<>();
        for(int i = 0; i < src.length; i+= 4){
            BitBuffer destBuffer = new BitBuffer((byte)0);
            BitBuffer srcBuffer = new BitBuffer(Arrays.copyOfRange(src,i,i+4));
            for(int j = 0; j < 4;j++){
                destBuffer.set(j * 2,srcBuffer.get(8 * (j + 1) - 2));
                destBuffer.set(j * 2 + 1 ,srcBuffer.get(8 * (j + 1) - 1));
            }
            if(destBuffer.getByte(0) == 0){
                break;
            }
            out.add(destBuffer.getByte(0));
        }
        byte[] outArray = new byte[out.size()];
        for (int i = 0;  i < out.size(); i++){
            outArray[i] = out.get(i);
        }
        return outArray;
    }

    public static void main(String[] args) {
        String str = "eeeeeeeeeeeeeeeee";
        System.out.println(Arrays.toString(str.getBytes(StandardCharsets.UTF_8)));
        System.out.println(Arrays.toString(hideData(str.getBytes(StandardCharsets.UTF_8),"cc".getBytes(StandardCharsets.UTF_8))));
        System.out.println(new String(hideData(str.getBytes(StandardCharsets.UTF_8),"cc".getBytes(StandardCharsets.UTF_8))));
        System.out.println(new String(getData("efdgefdgddddeeeee".getBytes(StandardCharsets.UTF_8))));
    }
}
