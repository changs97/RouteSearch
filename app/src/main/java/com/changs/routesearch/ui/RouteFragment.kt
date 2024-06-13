package com.changs.routesearch.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.changs.routesearch.data.model.PoiInfo
import com.changs.routesearch.databinding.FragmentRouteBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.MarkerIcons
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RouteFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentRouteBinding? = null
    private val binding get() = _binding!!

    private val mapViewModel: MapViewModel by viewModels()

    private var departureLocation: PoiInfo? = null
    private var destinationLocation: PoiInfo? = null

    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRouteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = binding.routeMapView
        mapView.onCreate(savedInstanceState)

        binding.routeImgClose.setOnClickListener {
            requireActivity().supportFragmentManager.setFragmentResult(
                "routeBackRequestKey", Bundle()
            )
        }

        mapView.getMapAsync(this)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                mapViewModel.pathPoints.collectLatest {
                    if (it.isNotEmpty() && mapViewModel.routeUiState.value.departureLocation != null) {
                        addMarker(
                            mapViewModel.routeUiState.value.departureLocation!!,
                            "출발지: ${mapViewModel.routeUiState.value.departureLocation!!.name}"
                        )
                        addMarker(
                            mapViewModel.routeUiState.value.destinationLocation!!,
                            "도착지: ${mapViewModel.routeUiState.value.destinationLocation!!.name}"
                        )

                        val path = PathOverlay()
                        path.coords = it
                        path.map = naverMap

                        val bounds = LatLngBounds.Builder()
                        bounds.include(it)

                        val cameraUpdate =
                            CameraUpdate.fitBounds(bounds.build(), 300) // padding을 조정할 수 있습니다.
                        naverMap.moveCamera(cameraUpdate)
                    }


                }
            }
        }

        arguments?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                departureLocation = it.getParcelable("departureLocation", PoiInfo::class.java)
                destinationLocation = it.getParcelable("destinationLocation", PoiInfo::class.java)
            } else {
                departureLocation = it.getParcelable("departureLocation")
                destinationLocation = it.getParcelable("destinationLocation")
            }

            if (departureLocation != null && destinationLocation != null) {
                mapViewModel.updateDepartureLocation(departureLocation!!)
                mapViewModel.updateDestinationLocation(destinationLocation!!)

                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        mapViewModel.routeUiState.collectLatest {
                            if (it.departureLocation != null && it.destinationLocation != null) {
                                mapViewModel.getRoutes()
                            }
                        }
                    }

                }
            }
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
    }

    private fun addMarker(location: PoiInfo, title: String) {
        val marker = Marker().apply {
            position = LatLng(location.lat.toDouble(), location.lon.toDouble())
            icon = MarkerIcons.BLACK
            map = naverMap

        }

        val infoWindow = InfoWindow().apply {
            adapter = object : InfoWindow.DefaultTextAdapter(requireContext()) {
                override fun getText(infoWindow: InfoWindow): CharSequence {
                    return title
                }
            }
            open(marker)
        }
    }
}