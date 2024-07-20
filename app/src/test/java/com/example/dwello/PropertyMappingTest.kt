//package com.example.yourapp
//
//import com.google.firebase.firestore.DocumentSnapshot
//import com.google.firebase.Timestamp
//import com.example.dwello.data.Property
//import org.junit.Test
//import org.junit.Assert.*
//import org.mockito.Mockito.*
//
//class PropertyMappingTest {
//
//    @Test
//    fun testPropertyMapping() {
//        // Example Firestore document data
//        val documentData = mapOf(
//            "about_home" to "Beautiful home in Bellevue",
//            "baths" to 6.5,
//            "beds" to 4,
//            "built_year" to 2016,
//            "city" to "Bellevue",
//            "est_monthly" to 42073,
//            "hoa" to 0,
//            "image_urls" to listOf("url1", "url2"),
//            "list_date" to Timestamp.now(),
//            "parking_space" to 4,
//            "price" to 1000000L,
//            "property_type" to "Single-family",
//            "state" to "WA",
//            "street" to "14024 NE 32nd Pl",
//            "sqft" to 3710,
//            "thumbnail_url" to "thumbnail_url",
//            "zipcode" to "98007",
//            "lat" to 47.640002750323376,
//            "lng" to -122.15240483138773
//        )
//
//        // Simulate a Firestore document
////        val document = mock(DocumentSnapshot::class.java)
////        `when`(document.data).thenReturn(documentData)
////        `when`(document.getString("about_home")).thenReturn("Beautiful home in Bellevue")
////        `when`(document.getDouble("baths")).thenReturn(6.5)
////        `when`(document.getLong("beds")).thenReturn(4L)
////        `when`(document.getLong("built_year")).thenReturn(2016L)
////        `when`(document.getString("city")).thenReturn("Bellevue")
////        `when`(document.getLong("est_monthly")).thenReturn(42073L)
////        `when`(document.getLong("hoa")).thenReturn(0L)
////        `when`(document.get("image_urls")).thenReturn(listOf("url1", "url2"))
////        `when`(document.getTimestamp("list_date")).thenReturn(Timestamp.now())
////        `when`(document.getLong("parking_space")).thenReturn(4L)
////        `when`(document.getLong("price")).thenReturn(1000000L)
////        `when`(document.getString("property_type")).thenReturn("Single-family")
////        `when`(document.getString("state")).thenReturn("WA")
////        `when`(document.getString("street")).thenReturn("14024 NE 32nd Pl")
////        `when`(document.getLong("sqft")).thenReturn(3710L)
////        `when`(document.getString("thumbnail_url")).thenReturn("thumbnail_url")
////        `when`(document.getString("zipcode")).thenReturn("98007")
////        `when`(document.getDouble("lat")).thenReturn(47.640002750323376)
////        `when`(document.getDouble("lng")).thenReturn(-122.15240483138773)
////        `when`(document.id).thenReturn("Jy9xc6aIemQsS4Fp58aG")
//        val document = mock(DocumentSnapshot::class.java)
//        `when`(document.data).thenReturn(documentData)
//        `when`(document.id).thenReturn("Jy9xc6aIemQsS4Fp58aG")
//
//
//        // Add logging before conversion
//        println("Document Data: ${document.data}")
//        println("Document ID: ${document.id}")
//
//        // Convert to Property object and handle nullability
//        val property = document.toObject(Property::class.java)?.copy(pid = document.id)
//
//        // Add logging after conversion
//        println("Property: $property")
//
//        // Check for null
//        assertNotNull(property)
//
//        // Assert fields are mapped correctly
//        property?.let {
//            assertEquals("Jy9xc6aIemQsS4Fp58aG", it.pid)
//            assertEquals("Beautiful home in Bellevue", it.about_home)
//            assertEquals(6.5, it.baths, 0.0)
//            assertEquals(4, it.beds)
//            assertEquals(2016, it.built_year)
//            assertEquals("Bellevue", it.city)
//            assertEquals(42073, it.est_monthly)
//            assertEquals(0, it.hoa)
//            assertEquals(listOf("url1", "url2"), it.image_urls)
//            assertNotNull(it.list_date)
//            assertEquals(4, it.parking_space)
//            assertEquals(1000000L, it.price)
//            assertEquals("Single-family", it.property_type)
//            assertEquals("WA", it.state)
//            assertEquals("14024 NE 32nd Pl", it.street)
//            assertEquals(3710, it.sqft)
//            assertEquals("thumbnail_url", it.thumbnail_url)
//            assertEquals("98007", it.zipcode)
//            assertEquals(47.640002750323376, it.lat, 0.0)
//            assertEquals(-122.15240483138773, it.lng, 0.0)
//        }
//    }
//}