package com.vinotec.healthclub;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends Activity implements LocationSource, AMapLocationListener {

	 private MapView mapView;
	 private AMap aMap;
	 private LocationManagerProxy mAMapLocationManager;
	 private OnLocationChangedListener mListener;
	 private LatLng oldPoint,newPoint;
	 
	 
	 TextView myLocation;
	 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// ����Ҫд
        init();
    }

    /**
     * ��ʼ��AMap����
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }
 
    private void setUpMap() {
    	
    	// �Զ���ϵͳ��λ����
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // �Զ��嶨λ����ͼ��
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.gps_location));
        // �Զ��徫�ȷ�Χ��Բ�α߿���ɫ
        myLocationStyle.strokeColor(Color.TRANSPARENT);
        //�Զ��徫�ȷ�Χ��Բ�α߿���
        myLocationStyle.strokeWidth(0);
        // �Զ��徫�ȷ�Χ��Բ���ڲ���ɫ
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);
        // ���Զ���� myLocationStyle ������ӵ���ͼ��
        aMap.setMyLocationStyle(myLocationStyle);
        //���ö�λ��������������ô˶�λ��Դ��λ��ť���ɵ����
        aMap.setLocationSource(this);
        //����Ĭ�϶�λ��ť�Ƿ���ʾ
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        // ����Ϊtrue��ʾ��ʾ��λ�㲢�ɴ�����λ��false��ʾ���ض�λ�㲢���ɴ�����λ��Ĭ����false
        aMap.setMyLocationEnabled(true);
        // ���ö�λ������Ϊ��λģʽ����λ��AMap.LOCATION_TYPE_LOCATE�������棨AMap.LOCATION_TYPE_MAP_FOLLOW��
        // ��ͼ������������ת��AMap.LOCATION_TYPE_MAP_ROTATE������ģʽ
        aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
        
    }
    
    /**
   	 * ����·��
   	 */
   	public void drawLine(LatLng oldPoint,LatLng newPoint) {
   	// ����һ������������
     aMap.addPolyline((new PolylineOptions())
     			.add(oldPoint, newPoint)
     			.width(10).geodesic(true)
     			.color(Color.argb(255, 1, 1, 1)));
   	}
    
    /**
	 * ��λ�ɹ���ص�����
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation!=null&&amapLocation.getAMapException().getErrorCode() == 0) {
			mListener.onLocationChanged(amapLocation);// ��ʾϵͳС����
			
			aMap.moveCamera(CameraUpdateFactory.zoomTo(16.5f));
			
			myLocation = (TextView)findViewById(R.id.myLocation);
			myLocation.setText(Double.toString(amapLocation.getLatitude())+"\n"+Double.toString(amapLocation.getLongitude()));
			
			newPoint = new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude());
			
			if(oldPoint != null){
				drawLine(oldPoint,newPoint);
			}
			oldPoint = newPoint;
			
			}
		}
	}
    
    /**
	 * ���λ
	 */
    
    @Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
        if (mAMapLocationManager == null) {
            mAMapLocationManager = LocationManagerProxy.getInstance(this);
            //�˷���Ϊÿ���̶�ʱ��ᷢ��һ�ζ�λ����Ϊ�˼��ٵ������Ļ������������ģ�
            //ע�����ú��ʵĶ�λʱ��ļ���������ں���ʱ�����removeUpdates()������ȡ����λ����
            //�ڶ�λ�������ں��ʵ��������ڵ���destroy()����     
            //����������ʱ��Ϊ-1����λֻ��һ��
            mAMapLocationManager.requestLocationData(
                    LocationProviderProxy.AMapNetwork, 4*1000, 10, this);
        }
	}

	/**
	 * ֹͣ��λ
	 */
    
	@Override
	public void deactivate() {
		 mListener = null;
	        if (mAMapLocationManager != null) {
	            mAMapLocationManager.removeUpdates(this);
	            mAMapLocationManager.destroy();
	        }
	        mAMapLocationManager = null;
	}
    
    @Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
    
    /**
     * ����������д
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
 
    /**
     * ����������д
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
     
    /**
     * ����������д
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
 
    /**
     * ����������д
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	

	
}
