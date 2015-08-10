package pavel.jsonlist;

import android.widget.ImageView;

//класс, нужный для передачи Person и ImageView из MyAdapterPerson в DownloadPicture для загрузки изображения
public class SetPicture {
    ImageView imageView;
    Person person;

    public SetPicture(ImageView imageView, Person person) {
        this.imageView = imageView;
        this.person = person;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public Person getPerson() {
        return person;
    }

}
