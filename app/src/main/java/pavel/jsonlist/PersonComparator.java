package pavel.jsonlist;

import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;

//далее, "Критерий" - установленный пользователем критерий, по которому будет вестись сортировка
//т.е. "Имя" или "Возраст" или "Адрес" и т.д.


//компаратор, который сравнивает Person'ы в зависимости от Настроек, установленных пользователем
class PersonComparator implements Comparator<Person> {

    ArrayList<String> sortBy=new ArrayList<>();
    ArrayList<Boolean> sortOrder=new ArrayList<>();

    public PersonComparator(ArrayList<String> sortBy, ArrayList<Boolean> sortOrder) {
        if(sortBy.size()==0)
            this.sortBy.add("");
        else this.sortBy = sortBy;

        if(sortOrder.size()==0)
            this.sortOrder.add(true);
        else this.sortOrder = sortOrder;
    }


    @Override
    public int compare(Person x, Person y) {

        //в зависимости от порядка Критериев, установленных в Настройках производит сравнение по
        //всем этим Критериям
        if (compareByMethod(x,y,sortBy.get(0))!=0){
            if(sortOrder.get(0))
                return compareByMethod(x, y, sortBy.get(0));
            else return compareByMethod(y, x, sortBy.get(0));
        } else if(compareByMethod(x, y, sortBy.get(1))!=0){
            if(sortOrder.get(1))
                return compareByMethod(x, y, sortBy.get(1));
            else return compareByMethod(y, x, sortBy.get(1));
        } else if(compareByMethod(x, y, sortBy.get(2))!=0){
            if(sortOrder.get(2))
                return compareByMethod(x, y, sortBy.get(2));
            else return compareByMethod(y, x, sortBy.get(2));
        } else if(compareByMethod(x, y, sortBy.get(3))!=0){
            if(sortOrder.get(3))
                return compareByMethod(x, y, sortBy.get(3));
            else return compareByMethod(y, x, sortBy.get(3));
        }else if(compareByMethod(x, y, sortBy.get(4))!=0){
            if(sortOrder.get(4))
                return compareByMethod(x, y, sortBy.get(4));
            else return compareByMethod(y, x, sortBy.get(4));
        }else if(compareByMethod(x, y, sortBy.get(5))!=0){
            if(sortOrder.get(5))
                return compareByMethod(x, y, sortBy.get(5));
            else return compareByMethod(y, x, sortBy.get(5));
        }else{
            if(sortOrder.get(6))
                return compareByMethod(x, y, sortBy.get(6));
            else return compareByMethod(y, x, sortBy.get(6));
        }
    }

    //возвращает результат сравнения двух Person по указанному полю (определяется названием метода)
    //"поле" - т.е. "Имя" или "Пол" или "Телефон" и т.д.
    //по названию метода, т.е., например для "Имени" - "getName", для "Возраста" - "getAge"
    private static int compareByMethod(Person p1, Person p2, String methodName){
        Method m=null;
        if(methodName.equals(""))
            return 0;
        try {
            //устанавливаем поле (Method m), по которому будем производить сравнение
            m = p1.getClass().getMethod(methodName, new Class[]{});
        } catch (Exception e){
            Log.d("method","exception1");
        }

        try {
            //сравнение по указанному полю, invoke вызывает соответствующий метод,
            // который возращает значение поля, например для "getName" вернётся "Василий", "getAge" - 28
            return compareTo(m.invoke(p1, new Object[]{}),m.invoke(p2, new Object[]{}));
        } catch (Exception e){
            Log.d("method","exception2");
        }
        return 0;
    }

    //возвращает результат сравнения для Boolean, Integer или String
    private static <T> int compareTo(T a,T b) throws Exception{
        if(a instanceof Integer){
            Integer aI=(Integer) a;
            Integer bI=(Integer) b;
            return aI.compareTo(bI);
        }
        else if(a instanceof Boolean){
            Boolean aB=(Boolean) a;
            Boolean bB=(Boolean) b;
            return aB.compareTo(bB);
        }
        else if(a instanceof String){
            String aS=(String) a;
            String bS=(String) b;
            return aS.compareTo(bS);
        }
        else{
            throw new Exception();
        }
    }
}