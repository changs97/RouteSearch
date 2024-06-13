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
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.MarkerIcons
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RouteFragment : Fragment() {
    private var _binding: FragmentRouteBinding? = null
    private val binding get() = _binding!!

    private val routeViewModel: RouteViewModel by viewModels()

    private lateinit var mapView: MapView

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

        mapView.getMapAsync { naverMap ->
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    routeViewModel.pathPoints.collectLatest { coords ->
                        if (coords.size >= 2) {
                            val path = PathOverlay()
                            path.coords = coords
                            path.map = naverMap

                            val bounds = LatLngBounds.Builder()
                            bounds.include(coords)

                            val cameraUpdate = CameraUpdate.fitBounds(bounds.build(), 300)

                            naverMap.moveCamera(cameraUpdate)
                        }
                    }
                }
            }

            arguments?.let {
                routeViewModel.run {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        departureLocation =
                            it.getParcelable("departureLocation", PoiInfo::class.java)
                        destinationLocation =
                            it.getParcelable("destinationLocation", PoiInfo::class.java)
                    } else {
                        departureLocation = it.getParcelable("departureLocation")
                        destinationLocation = it.getParcelable("destinationLocation")
                    }

                    if (departureLocation != null && destinationLocation != null) {
                        addMarker(
                            naverMap, departureLocation!!, "출발지: ${departureLocation!!.name}"
                        )

                        addMarker(
                            naverMap, destinationLocation!!, "도착지: ${destinationLocation!!.name}"
                        )

                        getRoutes(departureLocation!!, destinationLocation!!)
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

    private fun addMarker(naverMap: NaverMap, location: PoiInfo, title: String) {
        val marker = Marker().apply {
            position = LatLng(location.lat.toDouble(), location.lon.toDouble())
            icon = MarkerIcons.BLACK
            map = naverMap
        }

        InfoWindow().apply {
            adapter = object : InfoWindow.DefaultTextAdapter(requireContext()) {
                override fun getText(infoWindow: InfoWindow): CharSequence {
                    return title
                }
            }
            open(marker)
        }
    }
}