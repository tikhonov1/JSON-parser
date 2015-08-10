package pavel.jsonlist;




import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.view.Gravity;
import android.view.View;




import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragment;

//Фрагмент, показывающийся при неудавшейся попытке загрузить persons.json
public class NoConnectionFragment extends SherlockFragment {

    SharedPreferences sp;

    public NoConnectionFragment(SharedPreferences sp) {
        this.sp = sp;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_no_connection_layout, container, false);
        Button button=(Button)view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    showFragment(new BlankFragment());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


        return view;
    }
    private  void showFragment(Fragment currentFragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, currentFragment)
                .commit();
    }
}