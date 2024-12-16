package com.example.cloudmate.network.floodapi

data class FloodCheck(
    val location: String,                         // Địa điểm được kiểm tra
    val time: String,                             // Thời gian dưới dạng ISO 8601
    val status: String,                           // Trạng thái (Flood detected, resolved, etc.)
    val floodRelatedComments: List<String>,       // Danh sách bình luận về ngập lụt
    val resolvedFloodComments: List<String>,      // Danh sách bình luận về tình trạng đã giải quyết
    val ratios: Ratios
)
data class Ratios(
    val floodRatio: Double,                       // Tỷ lệ bình luận về ngập lụt
    val resolvedRatio: Double                     // Tỷ lệ bình luận về tình trạng đã giải quyết
)

