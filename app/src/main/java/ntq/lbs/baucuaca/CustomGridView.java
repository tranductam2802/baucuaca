package ntq.lbs.baucuaca;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

public class CustomGridView extends ArrayAdapter<Integer> {
	private Context context;
	private int resource;
	private Integer[] image;
	private Integer[] price = {0,100,200,300,400,500};
	private ArrayAdapter<Integer> adapter;
	
	public CustomGridView(Context context, int resource, Integer[] image) {
		super(context, resource, image);
		this.context = context;
		this.resource = resource;
		this.image = image;
		adapter = new ArrayAdapter<Integer>(context,android.R.layout.simple_spinner_item, price);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = View.inflate(context,resource, null);
		ImageView img = (ImageView) view.findViewById(R.id.img);
		Spinner price = (Spinner) view.findViewById(R.id.spin_price);
		
		img.setImageResource(image[position]);
		price.setAdapter(adapter);
		
		price.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int positionPrice, long id) {
				MainActivity.deal[position] = CustomGridView.this.price[positionPrice];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		return view;
	}
}