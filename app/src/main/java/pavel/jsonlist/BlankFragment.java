package pavel.jsonlist;

import android.content.SharedPreferences;
import android.content.res.Configuration;

import android.os.AsyncTask;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import java.util.Collections;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;


public class BlankFragment extends SherlockFragment {
    private ActionBar actionBar;
    private static RecyclerView rv;
    private static String LOG_TAG = "my_log";
    private SharedPreferences sp;
    static String strJsonStatic;
    static ProgressBar progressBar;

    //ArrayList людей, которых пользователь удалил
    public static ArrayList<Person> nonGrataPersonas=new ArrayList<>();

    //массив людей, загруженных из файла из интеренета
    private static ArrayList<Person> arrayOfPeopleArrayList=new ArrayList<>();


    //берёт из SharedPreferences настройки Названия критериев сортировки
    ArrayList<String> getSortByPreference(){
        ArrayList<String> sortByPref=new ArrayList<>();

        sortByPref.add(sp.getString("criter1", "getName"));
        sortByPref.add(sp.getString("criter2", ""));
        sortByPref.add(sp.getString("criter3", ""));
        sortByPref.add(sp.getString("criter4", ""));
        sortByPref.add(sp.getString("criter5", ""));
        sortByPref.add(sp.getString("criter6", ""));
        sortByPref.add(sp.getString("criter7", ""));

        return sortByPref;
    }

    //берёт из SharedPreferences настройки Порядок сортировки этих критериев
    ArrayList<Boolean> getSortOrderPreference(){
        ArrayList<Boolean> sortOrderPref=new ArrayList<>();

        sortOrderPref.add(!sp.getBoolean("chb1", true));
        sortOrderPref.add(!sp.getBoolean("chb2", true));
        sortOrderPref.add(!sp.getBoolean("chb3", true));
        sortOrderPref.add(!sp.getBoolean("chb4", true));
        sortOrderPref.add(!sp.getBoolean("chb5", true));
        sortOrderPref.add(!sp.getBoolean("chb6", true));
        sortOrderPref.add(!sp.getBoolean("chb7", true));

        return sortOrderPref;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        setHasOptionsMenu(true);

        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

        progressBar=(ProgressBar)view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        actionBar = getSherlockActivity().getSupportActionBar();
        actionBar.removeAllTabs();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle("JSON-парсер");


        rv = (RecyclerView)view.findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);

        //если мы уже загрузили всё из файла, то больше не загружать, сразу создать RecyclerView
        if(arrayOfPeopleArrayList.size()==0)
            new ParseTask().execute();
        else createRV();
        return view;
    }

    private class ParseTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader;
            String resultJson = "";

            //загружаем файл и парсим его в arrayOfPeopleArrayList из объектов <Person>
            try {
                Log.d(LOG_TAG, "connect1");
                URL url = new URL("http://109.120.187.164:81/people.json");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                Log.d(LOG_TAG, "connect2");
                Log.d(LOG_TAG, buffer.toString());

                resultJson = buffer.toString();

                inputStream.close();
                urlConnection.disconnect();
            } catch(Exception e){
                e.printStackTrace();

                //если произошла ошибка загрузки, перекидывает на NoConnectionFragment
                if(arrayOfPeopleArrayList.size()==0)
                    showFragment(new NoConnectionFragment(sp));
            }


            return resultJson;
        }

        //показывает выбранный фрагмент
        private  void showFragment(Fragment currentFragment) {
            FragmentManager fragmentManager = getFragmentManager();
            try {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, currentFragment)
                        .commit();
            } catch (Exception e){
                e.printStackTrace();
            }
        }


        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            progressBar.setVisibility(View.INVISIBLE);
            if(isAdded()){
                getResources().getString(R.string.app_name);
            }
            strJsonStatic=strJson;
            try {
                Log.d(LOG_TAG, "string" + strJsonStatic);

                JSONArray people = new JSONArray(strJsonStatic);

                people.getJSONObject(0);

                for (int i = 0; i < people.length(); i++) {
                    JSONObject person = people.getJSONObject(i);

                    String _id="";
                    boolean isActive=false;
                    String picture="";
                    int age=-1;
                    String name =getResources().getString(R.string.not_available);
                    String gender =getResources().getString(R.string.not_available);
                    String email =getResources().getString(R.string.not_available);
                    String phone =getResources().getString(R.string.not_available);
                    String address=getResources().getString(R.string.not_available);
                    String registered =getResources().getString(R.string.not_available);

                    try {
                        _id = person.getString("_id");
                        Log.d(LOG_TAG, "_id: " + _id);

                        try {
                            isActive = person.getBoolean("isActive");
                            Log.d(LOG_TAG, "isActive: " + isActive);
                        } catch (Exception e){e.printStackTrace();}

                        try {
                            picture = person.getString("picture");
                            Log.d(LOG_TAG, "picture: " + picture);
                        } catch (Exception e){e.printStackTrace();}

                        try {
                            age = person.getInt("age");
                            Log.d(LOG_TAG, "age: " + age);
                        } catch (Exception e){e.printStackTrace();}

                        try {
                            name=person.getString("name");
                            Log.d(LOG_TAG, "name: " + name);
                        } catch (Exception e){e.printStackTrace();}

                        try {
                            gender = person.getString("gender");
                            Log.d(LOG_TAG, "gender: " + gender);
                        } catch (Exception e){e.printStackTrace();}

                        try {
                            email = person.getString("email");
                            Log.d(LOG_TAG, "email: " + email);
                        } catch (Exception e){e.printStackTrace();}

                        try {
                            phone = person.getString("phone");
                            Log.d(LOG_TAG, "phone: " + phone);
                        } catch (Exception e){e.printStackTrace();}

                        try {
                            address = person.getString("address");
                            Log.d(LOG_TAG, "address: " + address);
                        } catch (Exception e){e.printStackTrace();}

                        try {
                            registered = person.getString("registered");
                            Log.d(LOG_TAG, "registered: " + registered);
                        } catch (Exception e){e.printStackTrace();}

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    Person p=new Person(_id, isActive, picture, age, name,
                            gender, email, phone, address, registered);
                    arrayOfPeopleArrayList.add(p);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            createRV();
        }
    }

    //создаёт RecyclerView из arrayOfPeopleArrayList:
    //  1 столбец на вертикальную ориентацию
    //  2 столбца на горизонтальную
    void createRV(){
        try {
            GridLayoutManager llm;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                llm = new GridLayoutManager(getActivity(), 2);
            else llm = new GridLayoutManager(getActivity(), 1);
            rv.setLayoutManager(llm);

            arrayOfPeopleArrayList.removeAll(nonGrataPersonas);
            Collections.sort(arrayOfPeopleArrayList, new PersonComparator(getSortByPreference(), getSortOrderPreference()));
            MyAdapterPerson adapter = new MyAdapterPerson(arrayOfPeopleArrayList,nonGrataPersonas);
            rv.setAdapter(adapter);
            rv.setItemAnimator(new DefaultItemAnimator());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}