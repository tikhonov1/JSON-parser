package pavel.jsonlist;


import android.content.SharedPreferences;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;

import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


//далее, "Критерий" - установленный пользователем критерий, по которому будет вестись сортировка
//т.е. "Имя" или "Возраст" или "Адрес" и т.д.
//
//"Уровень" сортировки - означает, по какому Критерию будет отсортированно сначала, по которому после


//Фрейм с настройками сортировки
public class SortSettings extends android.support.v4.preference.PreferenceFragment {



    private static ArrayList<Preference> criteriesArray;
    private static ArrayList<Preference> chbArray;

    private static SharedPreferences prefs;
    private static HashMap<String,String> hashMap=new HashMap<>();

    private static HashSet<String> usedCriteries=new HashSet<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        usedCriteries.clear();

        //соответсвие между выводимым названием и реальным методом в классе Person
        hashMap.put("getName", "Имя");
        hashMap.put("getAge","Возраст");
        hashMap.put("getGender","Пол");
        hashMap.put("getIsActive","Активность");
        hashMap.put("getPhone","Телефон");
        hashMap.put("getEmail", "Email");
        hashMap.put("getAddress","Адрес");

        addPreferencesFromResource(R.xml.pref_general);
        final Preference myPref = (Preference) findPreference("sort_deep");
        criteriesArray=new ArrayList<>();
        chbArray=new ArrayList<>();


        //добавление в массив Критериев настройки из layout'а
        criteriesArray.add((Preference) findPreference("criter1"));
        criteriesArray.add((Preference) findPreference("criter2"));
        criteriesArray.add((Preference) findPreference("criter3"));
        criteriesArray.add((Preference) findPreference("criter4"));
        criteriesArray.add((Preference) findPreference("criter5"));
        criteriesArray.add((Preference) findPreference("criter6"));
        criteriesArray.add((Preference) findPreference("criter7"));

        //то же самое для Порядка сортировки
        chbArray.add((Preference) findPreference("chb1"));
        chbArray.add((Preference) findPreference("chb2"));
        chbArray.add((Preference) findPreference("chb3"));
        chbArray.add((Preference) findPreference("chb4"));
        chbArray.add((Preference) findPreference("chb5"));
        chbArray.add((Preference) findPreference("chb6"));
        chbArray.add((Preference) findPreference("chb7"));

        for (final Preference p:criteriesArray){
            p.setSummary(hashMap.get(prefs.getString(p.getKey(), "1")));
        }

        initUsedCriteries();
        setEntriesForAll();

        //устанавливает лисенеры на Критерии
        for (final Preference p:criteriesArray){
            p.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    p.setSummary(hashMap.get(newValue.toString()));
                    usedCriteries.add(newValue.toString());
                    usedCriteries.remove(prefs.getString(p.getKey(), ""));
                    setEntriesForAll();
                    return true;
                }
            });
        }

        setEnabled(Integer.parseInt(prefs.getString("sort_deep", "1")));

        //по смене значения "Вложенности сортировки" меняет ListPrefernces'ы на Enabled или Disabled
        myPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                setEnabled(Integer.parseInt(newValue.toString()));

                initUsedCriteries();
                setEntriesForAll();

                return true;
            }
        });
    }

    //устанавливает Enabled для тех ListPreference, которые меньше установленной пользователем sortDeep - вложенности сортировки
    private static void setEnabled(int sortDeep){
        for(int i=0;i<7;i++){
            if(i<sortDeep) {
                criteriesArray.get(i).setEnabled(true);
                chbArray.get(i).setEnabled(true);
            }else {
                criteriesArray.get(i).setEnabled(false);
                chbArray.get(i).setEnabled(false);
                criteriesArray.get(i).setSummary("");
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(criteriesArray.get(i).getKey(), "");
                editor.commit();
            }
        }
    }

    //устанавливает в usedCriteries использованные Критерии
    private static void initUsedCriteries(){
        usedCriteries.clear();
        for (final Preference p : criteriesArray) {
            String s=prefs.getString(p.getKey(), "");
            if (!s.equals(""))
                usedCriteries.add(s);
        }
        return;
    }

    //возращает ещё не использованные Критерии на русском языке (не названия методов)
    private static ArrayList<String> getNotUsedForHuman(){
        ArrayList<String> notUsed=new ArrayList<>();
        notUsed.add("getName");
        notUsed.add("getAge");
        notUsed.add("getGender");
        notUsed.add("getIsActive");
        notUsed.add("getPhone");
        notUsed.add("getEmail");
        notUsed.add("getAddress");

        for (String s:usedCriteries)
            notUsed.remove(s);

        for (int i=0;i<notUsed.size();i++)
            notUsed.set(i,hashMap.get(notUsed.get(i)));

        return notUsed;
    }

    //возвращает НЕ использованные Критерии
    private static ArrayList<String> getNotUsed(){
        ArrayList<String> notUsed=new ArrayList<>();
        notUsed.add("getName");
        notUsed.add("getAge");
        notUsed.add("getGender");
        notUsed.add("getIsActive");
        notUsed.add("getPhone");
        notUsed.add("getEmail");
        notUsed.add("getAddress");

        for (String s:usedCriteries)
            notUsed.remove(s);

        return notUsed;
    }

    //устанавливает в ListPreference список показанных пользователю вариантов выбора Критериев
    //в этот список не включаются те Критерии, которые уже были использованы на других Уровнях
    private static void setEntriesForAll(){
        for (Preference p:criteriesArray){
            ListPreference lP=(ListPreference) p;
            lP.setEntries(getNotUsedForHuman().toArray(new CharSequence[getNotUsedForHuman().size()]));
            lP.setEntryValues(getNotUsed().toArray(new CharSequence[getNotUsed().size()]));
        }
    }


}
