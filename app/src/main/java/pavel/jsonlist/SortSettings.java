package pavel.jsonlist;


import android.content.SharedPreferences;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;

import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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

        criteriesArray.add((Preference) findPreference("criter1"));
        criteriesArray.add((Preference) findPreference("criter2"));
        criteriesArray.add((Preference) findPreference("criter3"));
        criteriesArray.add((Preference) findPreference("criter4"));
        criteriesArray.add((Preference) findPreference("criter5"));
        criteriesArray.add((Preference) findPreference("criter6"));
        criteriesArray.add((Preference) findPreference("criter7"));

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
        myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                setEnabled(Integer.parseInt(prefs.getString("sort_deep", "1")));
                initUsedCriteries();
                return true;
            }
        });

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

    private static void initUsedCriteries(){
        usedCriteries.clear();
        for (final Preference p : criteriesArray) {
            String s=prefs.getString(p.getKey(), "");
            if (!s.equals(""))
                usedCriteries.add(s);
        }
        return;
    }

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
    private static void setEntriesForAll(){
        for (Preference p:criteriesArray){
            ListPreference lP=(ListPreference) p;
            lP.setEntries(getNotUsedForHuman().toArray(new CharSequence[getNotUsedForHuman().size()]));
            lP.setEntryValues(getNotUsed().toArray(new CharSequence[getNotUsed().size()]));
        }
    }


}
