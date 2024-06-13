package com.changs.routesearch.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.changs.routesearch.databinding.FragmentHomeBinding
import com.changs.routesearch.util.Constant.LOCATION_PERMISSION_REQUEST_CODE
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource


class HomeFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            naverMap.uiSettings.isLocationButtonEnabled = true
            naverMap.locationTrackingMode = LocationTrackingMode.Follow
        } else {
            naverMap.uiSettings.isLocationButtonEnabled = false
            naverMap.locationTrackingMode = LocationTrackingMode.None
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        mapView = binding.homeMapView
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync(this)

        binding.homeTxtSearchBar.setOnClickListener {
            requireActivity().supportFragmentManager.setFragmentResult(
                "homeSearchRequestKey", Bundle()
            )
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
        _binding = null
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.uiSettings.isCompassEnabled = false
        naverMap.locationSource = locationSource

        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            naverMap.uiSettings.isLocationButtonEnabled = true
            naverMap.locationTrackingMode = LocationTrackingMode.Follow
        } else {
            naverMap.uiSettings.isLocationButtonEnabled = false
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}