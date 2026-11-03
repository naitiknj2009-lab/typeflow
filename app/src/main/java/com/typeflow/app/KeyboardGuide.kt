package com.typeflow.app
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun KeyboardGuide() {
 val rows=listOf("Q W E R T Y U I O P","A S D F G H J K L ;","Z X C V B N M , . /")
 val left=setOf('Q','A','Z','W','S','X','E','D','C','R','F','V','T','G','B')
 androidx.compose.foundation.layout.Column(verticalArrangement=Arrangement.spacedBy(4.dp)) {
  rows.forEach { row ->
   Row(horizontalArrangement=Arrangement.spacedBy(3.dp)) {
    row.split(" ").forEach { key ->
     val color=if(key.first() in left) Color(0xFFE5EDFF) else Color(0xFFFFE8D8)
     Text(key,Modifier.background(color,RoundedCornerShape(5.dp)).padding(horizontal=5.dp,vertical=4.dp),fontSize=10.sp,color=Color(0xFF243247))
    }
   }
  }
  Text("Blue: left hand     Peach: right hand",fontSize=9.sp,color=Color.Gray)
 }
}
