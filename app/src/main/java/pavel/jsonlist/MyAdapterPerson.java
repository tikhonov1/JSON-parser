package pavel.jsonlist;

import android.annotation.SuppressLint;
import android.content.Context;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


@SuppressLint("InflateParams")
public class MyAdapterPerson extends RecyclerView.Adapter<MyAdapterPerson.ViewHolder>  {


    private static ArrayList<Person> people;
    private static ArrayList<Person> nonGrata;
    public static Context C;


    public MyAdapterPerson(ArrayList<Person> people,ArrayList<Person> nonGrata) {
        this.people = people;
        this.nonGrata=nonGrata;

    }

    @Override
    public MyAdapterPerson.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {

        C=parent.getContext();
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {


        viewHolder.personPhoto.setImageResource(R.drawable.empty);
        viewHolder.personName.setText(people.get(position).getName());

        viewHolder.personGender.setText("   Gender: " + people.get(position).getGender());
        viewHolder.personEmail.setText(people.get(position).getEmail());
        viewHolder.personPhone.setText(people.get(position).getPhone());
        viewHolder.personAddress.setText(people.get(position).getAddress());
        viewHolder.personRegistered.setText(people.get(position).getRegistered());


        if(people.get(position).getAge()==-1)
            viewHolder.personAge.setText("Age: "+C.getResources().getString(R.string.not_available));
        else viewHolder.personAge.setText("Age: "+people.get(position).getAge());

        if(people.get(position).getIsActive())
            viewHolder.personIsActive.setImageResource(R.drawable.yes_small);
        else viewHolder.personIsActive.setImageResource(R.drawable.no_small);


        String URL = people.get(position).getPicture();
            viewHolder.personPhoto.setTag(URL);

        viewHolder.personPhoto.buildDrawingCache();

        if(people.get(position).getPictureBitmap()==null) {
            DownloadPicture task=new DownloadPicture();
            task.execute(new SetPicture(viewHolder.personPhoto, people.get(position)));
        }
        else viewHolder.personPhoto.setImageBitmap(people.get(position).getPictureBitmap()
        );

        viewHolder.cv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Person p=people.remove(position);
                people.remove(p);
                nonGrata.add(p);

                Toast toast = Toast.makeText(C,
                        "Удалено", Toast.LENGTH_SHORT);
                toast.show();
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, people.size());

                return true;
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public CardView cv;

        public TextView personName;
        public TextView personAge;
        public TextView personGender;
        public TextView personEmail;
        public TextView personPhone;
        public TextView personAddress;
        public TextView personRegistered;

        public ImageView personPhoto;
        public ImageView personIsActive;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            cv=(CardView)itemView.findViewById(R.id.cv);

            personName = (TextView)itemView.findViewById(R.id.name);
            personAge = (TextView)itemView.findViewById(R.id.age);
            personPhoto = (ImageView)itemView.findViewById(R.id.picture);
            personGender = (TextView)itemView.findViewById(R.id.gender);
            personEmail= (TextView)itemView.findViewById(R.id.email);
            personPhone= (TextView)itemView.findViewById(R.id.phone);
            personAddress= (TextView)itemView.findViewById(R.id.address);
            personRegistered= (TextView)itemView.findViewById(R.id.registered);
            personIsActive= (ImageView)itemView.findViewById(R.id.isActive);

        }
    }

    @Override
    public int getItemCount() {
        return people.size();
    }
}