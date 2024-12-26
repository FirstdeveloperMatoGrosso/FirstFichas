package br.com.firstingressos.dashboard.sdkdemo.activities.ingressos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidmads.library.qrgenearator.QRGEncoder;
import br.com.firstingressos.dashboard.R;

public class ProdutoAdapter extends ArrayAdapter<Evento> {
    private Context mContext;
    private int mResource;
    ConstraintLayout expandableView;
    QRGEncoder qrgEncoder;
    CardView cardView;
    RelativeLayout badge;

    public ProdutoAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Evento> objects) {
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
        TextView txtQtd = convertView.findViewById(R.id.txtQtd);
        badge = convertView.findViewById(R.id.badge);

        try {
//            imageView.setImageBitmap(BitmapFactory.decodeStream(new URL(getItem(position).getImage()).openConnection().getInputStream()));
            Picasso.get().load(getItem(position).getImage()).into(imageView);
        } catch (Exception e) {
            int idImg = getContext().getResources().getIdentifier("fijek_4", "drawable", getContext().getPackageName());
            imageView.setImageResource(idImg);
        }

        txtName.setText(getItem(position).getName());
        txtAddress.setText(getItem(position).getAddress());
        txtValor.setText(getItem(position).getValor());
        txtTaxa.setText(getItem(position).getTaxa());
        id.setText(getItem(position).getId());
        txtData.setText(getItem(position).getData());
        txtTipo.setText(getItem(position).getTipo());
        txtQtd.setText(getItem(position).getQtd());

        try {
            if (getItem(position).getQtd() == "" || getItem(position).getQtd() == "0")
                badge.setVisibility(View.GONE);
        }catch(Exception e){

        }

        return convertView;
    }
}
