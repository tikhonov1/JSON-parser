package pavel.jsonlist;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


//далее, "Критерий" - установленный пользователем критерий, по которому будет вестись сортировка
//т.е. "Имя" или "Возраст" или "Адрес" и т.д.


public class MainActivity extends SherlockFragmentActivity {
    private static boolean isInMainScreen=true;
    public static boolean isFirst=true;

    SlidingMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_layout);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //делает Слайдинг меню
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        menu.setMenu(R.layout.sidemenu);
        menu.setBehindWidthRes(R.dimen.slidingmenu_behind_width);

        //при запуске программы устанавливает фрейм на BlankFragment, в котором RecyclerView
        if(isFirst)
            showFragment(new BlankFragment());

        String[] items = getResources().getStringArray(R.array.slider_names);
        ((ListView) findViewById(R.id.sidemenu)).setAdapter(
            new SliderAdapter(this, items)
        );

        //лисенер на нажатие в Слайдинг-меню
        ((ListView) findViewById(R.id.sidemenu)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //меняет фрагмент на выбранный
                changeFragment(position);
                menu.showContent();
            }
        });

            isFirst=false;
    }


    //если нажали кнопку Назад
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //если Слайдинг меню было открыто, то закрываем его
            if(menu.isMenuShowing()){
                menu.toggle(true);
                return false;
            }
            //если мы сейчас НЕ на Главном экране (на котором Recyclerview), то переключаемя на него
            if(!isInMainScreen){
                isInMainScreen=true;
                showFragment(new BlankFragment());
                return false;

            } else{ //если мы на Главном экране нажали Назад, выходим из приложения

                System.exit(0);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    //меняет фрагмент на выбранный
    private void changeFragment(int position) {
        switch (position) {
            case 0:
                if(isInMainScreen) {
                    menuToggle();
                    break;
                }

                isInMainScreen=true;
                invalidateOptionsMenu();
                showFragment(new BlankFragment());
                break;
            case 1:
                isInMainScreen=false;
                invalidateOptionsMenu();
                showFragment(new SortSettings());
                break;
        }
    }

    //показывает выбранный фрагмент
    private void showFragment(Fragment currentFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, currentFragment)
                .commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                menuToggle();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //открывает или закрывает меню
    public void menuToggle(){
        if(menu.isMenuShowing())
            menu.showContent();
        else
            menu.showMenu();
    }


    //адаптер для Слайдинг-меню, устанавливает текст элементов и иконки для этих элементов
    public class SliderAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final String[] values;

        public SliderAdapter(Context context, String[] values) {
            super(context, R.layout.sidemenu_item, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.sidemenu_item, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.text);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.my_icon);
            textView.setText(values[position]);
            String s = values[position];
            if(s.startsWith(getResources().getString(R.string.sort_settings))){
                imageView.setImageResource(R.drawable.setting_small);
            } else if(s.startsWith(getResources().getString(R.string.main_screen))){
                imageView.setImageResource(R.drawable.home_small);
            }
            return rowView;
        }
    }
}
