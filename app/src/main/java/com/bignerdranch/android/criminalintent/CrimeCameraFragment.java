package com.bignerdranch.android.criminalintent;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created  on 24.12.2014.
 */
public class CrimeCameraFragment extends Fragment {
	private static final String TAG = "CrimeCameraFragment";
	private Camera      mCamera;
	private SurfaceView mSurfaceView;
	private View        mProgressContainer;


	private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
		public void onShutter() {
			// Отображение индикатора прогресса
			mProgressContainer.setVisibility(View.VISIBLE);
		}
	};

	private Camera.PictureCallback mJpegCallback = new Camera.PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
	// Создание имени файла
			String filename = UUID.randomUUID().toString() + ".jpg";
	// Сохранение данных jpeg на диске
			FileOutputStream os = null;
			boolean success = true;
			try {
				os = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
				os.write(data);
			} catch (Exception e) {
				Log.e(TAG, "Error writing to file " + filename, e);
				success = false;
			} finally {
				try {
					if (os != null)
						os.close();
				} catch (Exception e) {
					Log.e(TAG, "Error closing file " + filename, e);
					success = false;
				}
			}
			if (success) {
				Log.i(TAG, "JPEG saved at " + filename);
			}
			getActivity().finish();
		}
	};


	/** Простой алгоритм для получения наибольшего доступного размера.
	 * Более мощная версия представлена в файле CameraPreview.java
	 * приложения-примера ApiDemos от Android. */
	private Camera.Size getBestSupportedSize(List<Camera.Size> sizes, int width, int height) {
		Camera.Size bestSize = sizes.get(0);
		int largestArea = bestSize.width * bestSize.height;
		for (Camera.Size s : sizes) {
			int area = s.width * s.height;
			if (area > largestArea) {
				bestSize = s;
				largestArea = area;
			}
		}
		return bestSize;
	}

	@Override
	@SuppressWarnings("deprecation")
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_crime_camera, parent, false);
		mProgressContainer = v.findViewById(R.id.crime_camera_progressContainer);
		mProgressContainer.setVisibility(View.INVISIBLE);


		Button takePictureButton = (Button) v
				.findViewById(R.id.crime_camera_takePictureButton);
		takePictureButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (mCamera != null) {
					mCamera.takePicture(mShutterCallback, null, mJpegCallback);
				}
			}
		});
		mSurfaceView = (SurfaceView) v.findViewById(R.id.crime_camera_surfaceView);
		SurfaceHolder holder = mSurfaceView.getHolder();
		// Метод setType() и константа SURFACE_TYPE_PUSH_BUFFERS считаются
		// устаревшими, но они необходимы для того, чтобы функция
		// предварительного просмотра изображения с камеры
		// Camera работала на устройствах до 3.0.
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holder.addCallback(new SurfaceHolder.Callback() {
			public void surfaceCreated(SurfaceHolder holder) {
			// Приказываем камере использовать указанную
			// поверхность как область предварительного просмотра
				try {
					if (mCamera != null) {
						mCamera.setPreviewDisplay(holder);
					}
				} catch (IOException exception) {
					Log.e(TAG, "Error setting up preview display", exception);
				}
			}
			public void surfaceDestroyed(SurfaceHolder holder) {
				// Дальнейший вывод на поверхности невозможен,
				// прекращаем предварительный просмотр.
				if (mCamera != null) {
					mCamera.stopPreview();
				}
			}
			public void surfaceChanged(SurfaceHolder holder, int format, int w, int h)
			{
				if (mCamera == null) return;
				// Размер поверхности изменился; обновить размер
				// области предварительного просмотра камеры
				Camera.Parameters parameters = mCamera.getParameters();

				Camera.Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(), w, h);
				parameters.setPreviewSize(s.width, s.height);
				s = getBestSupportedSize(parameters.getSupportedPictureSizes(), w, h);
				parameters.setPictureSize(s.width, s.height);
				mCamera.setParameters(parameters);
				try {
					mCamera.startPreview();
				} catch (Exception e) {
					Log.e(TAG, "Could not start preview", e);
					mCamera.release();
					mCamera = null;
				}
			}
		});
		return v;
	}

	@TargetApi(9)
	@Override
	public void onResume() {
		super.onResume();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			mCamera = Camera.open(0);
		} else {
			mCamera = Camera.open();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}

}
