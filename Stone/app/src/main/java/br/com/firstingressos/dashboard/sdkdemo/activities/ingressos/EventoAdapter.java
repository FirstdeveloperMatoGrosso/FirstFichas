package br.com.firstingressos.dashboard.sdkdemo.activities.ingressos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import br.com.firstingressos.dashboard.R;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

public class EventoAdapter extends ArrayAdapter<Evento> {
    private Context mContext;
    private int mResource;
    ConstraintLayout expandableView;
    QRGEncoder qrgEncoder;
    CardView cardView;
    public EventoAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Evento> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        convertView = layoutInflater.inflate(mResource, parent, false);

        ImageView imageView = convertView.findViewById(R.id.image);
        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtAddress = convertView.findViewById(R.id.txtAddress);
        TextView txtValor = convertView.findViewById(R.id.txtValor);
        TextView txtTaxa = convertView.findViewById(R.id.txtTaxa);
        TextView id = convertView.findViewById(R.id.id);
        TextView txtData = convertView.findViewById(R.id.txtData);
        TextView txtTipo = convertView.findViewById(R.id.txtTipo);

        int width = 200;
        int height = 200;
        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;
        qrgEncoder = new QRGEncoder(getItem(position).getId(), null, QRGContents.Type.TEXT, dimen);
//            imageView.setImageBitmap(BitmapFactory.decodeStream(new URL(getItem(position).getImage()).openConnection().getInputStream()));
        imageView.setImageBitmap(qrgEncoder.getBitmap());

        txtName.setText(getItem(position).getName());
        txtAddress.setText(getItem(position).getAddress());
        txtValor.setText(getItem(position).getValor());
        txtTaxa.setText(getItem(position).getTaxa());
        id.setText(getItem(position).getId());
        txtData.setText(getItem(position).getData());
        txtTipo.setText(getItem(position).getTipo());

//        expandableView = convertView.findViewById(R.id.expandableView);
//        cardView = convertView.findViewById(R.id.cardView);

        return convertView;
    }
}
