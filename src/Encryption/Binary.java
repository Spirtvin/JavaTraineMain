package Encryption;

import Common.Constants.Messages;
import Encryption.TruthTables.Tables;
import Helpers.Arrays;
import Helpers.Converter;

/**
 * Двоичное число
 */
public class Binary {

    //<editor-fold desc="Fields">
    private Arrays<Boolean> helper = new Arrays<Boolean>();

    private Boolean[] bits;
    //</editor-fold>

    //<editor-fold desc="Construtors">
    public Binary() {
        this.bits = new Boolean[]{};
    }

    public Binary(Boolean[] bits) {
        this.bits = bits;
    }

    public Binary(Integer value) throws Exception {
        this.bits = this.ToBin(value).GetBits();
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    public Boolean Get(int index) throws Exception {
        if (helper.Check(this.bits, index))
            return this.bits[index];
        else
            throw new Exception(Messages.Exceptions.indexIncorrect);
    }

    public Boolean[] GetBits() {
        Arrays<Boolean> helper = new Arrays<>();
        return helper.Copy(this.bits);
    }

    public Integer GetLength() {
        return this.bits.length;
    }
    //</editor-fold>

    //<editor-fold desc="Setters">
    public void Set(int index, Boolean value) throws Exception {
        if (helper.Check(this.bits, index))
            this.bits[index] = value;
        else
            throw new Exception(Messages.Exceptions.indexIncorrect);

    }

    public void Set(int index, Integer value) throws Exception {
        if (value > -1 && value < 2)
            this.Set(index, value);
        else
            throw new Exception(Messages.Exceptions.valueIncorrect);
    }
    //</editor-fold>

    //<editor-fold desc="Logic Operations">

    /**
     * Исключающее или
     *
     * @param value
     * @return
     * @throws Exception
     */
    public Binary XOR(Binary value) throws Exception {
        Integer length = Math.max(this.GetLength(), value.GetLength());
        Binary b1 = this.ToNBit(length);
        Binary b2 = value.ToNBit(length);
        Binary result = new Binary();
        result = result.ToNBit(length);
        for (int i = 0; i < length; i++)
            result.Set(i, Tables.XOR(b1.Get(i), b2.Get(i)));
        return result;
    }

    /**
     * Логическое И
     *
     * @param value
     * @return
     * @throws Exception
     */
    public Binary AND(Binary value) throws Exception {
        Integer length = Math.max(this.GetLength(), value.GetLength());
        Binary b1 = this.ToNBit(length);
        Binary b2 = value.ToNBit(length);
        Binary result = new Binary();
        result = result.ToNBit(length);
        for (int i = 0; i < length; i++)
            result.Set(i, Tables.AND(b1.Get(i), b2.Get(i)));
        return result;
    }

    /**
     * Логическое ИЛИ
     *
     * @param value
     * @return
     * @throws Exception
     */
    public Binary OR(Binary value) throws Exception {
        Integer length = Math.max(this.GetLength(), value.GetLength());
        Binary b1 = this.ToNBit(length);
        Binary b2 = value.ToNBit(length);
        Binary result = new Binary();
        result = result.ToNBit(length);
        for (int i = length - 1; i >= 0; i--)
            result.Set(i, Tables.OR(b1.Get(i), b2.Get(i)));
        return result;
    }

    /**
     * Циклический сдвиг влево
     *
     * @return
     */
    public Binary LeftShift() {
        Boolean[] result = this.GetBits();
        Boolean tmp = result[0];
        for (int i = 0; i < this.bits.length - 1; i++)
            result[i] = result[i + 1];
        result[this.bits.length - 1] = tmp;
        return new Binary(result);
    }

    /**
     * Циклический сдвиг вправо
     *
     * @return
     */
    public Binary RightShift() {
        Boolean[] result = this.GetBits();
        Boolean tmp = result[result.length - 1];
        for (int i = this.bits.length - 1; i > 0; i--)
            result[i] = result[i - 1];
        result[0] = tmp;
        return new Binary(result);
    }

//    public Binary Swap(HashMap<Integer,Integer> map) throws Exception {
//        //TODO: сделать в любом двоичном числе перестановку по указанной таблице перестановок "E"
//        //Создать бинарное число размером с количество ячеек таблицы
//        //заполнить биты в соответсвии с таблицей
//        //вернуть значение
//        //Binary res = new Binary(map.size());
//    }

    //</editor-fold>

    //<editor-fold desc="Converters">
    public Binary ToNBit(int size) throws Exception {
        if (size > 0) {
            if (size == this.GetLength())
                return new Binary(this.bits);
            Boolean[] result = new Boolean[size];
            for (int i = 0; i < size; i++)
                result[i] = false;
            if (this.bits.length < size) {
                for (int i = 0; i < this.bits.length; i++)
                    result[result.length - 1 - i] = this.bits[this.bits.length - 1 - i];
            } else {
                for (int i = 0; i < size; i++)
                    result[size - 1 - i] = this.bits[size - 1 - i];
            }

            return new Binary(result);
        } else
            throw new Exception(Messages.Exceptions.sizeNegative);
    }

    /**
     * Переводит число в двоичный вид
     *
     * @param value значение для перевода
     * @return
     */
    private Binary ToBin(Integer value) throws Exception {
        Binary result = new Binary();
        result = result.ToNBit(Integer.SIZE);
        int i = Integer.SIZE - 1;
        while (value > 0) {
            result.Set(i, Converter.Integers.Convert(value % 2));
            value = value >> 1;
            i--;
        }
        return result;
    }

    /**
     * Переводит число из двоичного кода в десятичный
     *
     * @return
     */
    public Integer ToInt() throws Exception {
        Integer result = 0;
        int i = this.GetLength() - 1;
        while (i > -1) {
            result += Converter.Booleans.Convert(this.Get(i));
            result = result << 1;
            i--;
        }
        return result;
    }
    //</editor-fold>

    //<editor-fold desc="Other">

    /**
     * Проверяет колчество единиц на четность
     * @return true -если содержит четное число бит (единиц)
     * @throws Exception
     */
    public Boolean Parity() throws Exception {
        if (this.bits.length == 0)
            throw new Exception(Messages.Exceptions.binaryEmpty);
        Integer count = 0;
        for (int i = 0; i < this.bits.length; i++)
            if (this.bits[i] == true)
                count++;
        return count%2==0;
    }

    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < this.bits.length; i++) {
            if (i % 4 == 0 && i > 0)
                str += " ";
            str += Converter.Booleans.Convert(this.bits[i]);
        }
        return str;
    }
    //</editor-fold>
}
