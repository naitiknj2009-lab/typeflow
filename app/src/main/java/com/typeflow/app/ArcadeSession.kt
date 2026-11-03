package com.typeflow.app
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun ArcadeSession(mode:String,exit:()->Unit,complete:(TypingResult)->Unit) {
 val target=remember(mode){when(mode){
  "Word Sprint"->("bright calm focus rhythm steady learn type skill quick hands clear words smooth practice progress ").repeat(18).trim()
  "Sentence Flow"->"Every practice session is a chance to improve. Focus on accuracy, stay relaxed, and enjoy the progress you make. Good habits turn careful practice into confident typing."
  "Accuracy Quest"->"accuracy before speed; careful fingers make fewer mistakes; return to home position; type each character with control."
  else->"123 456 7890! @ # \$ % & * ( ) Version 2.5 is ready. Use 10, 20, and 30 as examples; save 15% today!"
 }}
 val timed=mode=="Word Sprint"||mode=="Number & Symbol Mix"
 val limit=if(mode=="Number & Symbol Mix")60 else 45
 var typed by remember(mode){mutableStateOf("")}
 var errors by remember(mode){mutableIntStateOf(0)}
 var seconds by remember(mode){mutableIntStateOf(0)}
 var paused by remember(mode){mutableStateOf(false)}
 var ended by remember(mode){mutableStateOf(false)}
 val focus=remember{FocusRequester()}
 val correct=typed.indices.count{typed[it]==target[it]}
 val accuracy=if(typed.isEmpty())100 else correct*100/typed.length
 val wpm=if(seconds==0)0 else (correct/5f/(seconds/60f)).toInt()
 fun finish(){if(!ended){ended=true;complete(TypingResult(correct,errors,seconds.coerceAtLeast(1),typed.length))}}
 LaunchedEffect(paused,ended){while(!paused&&!ended){delay(1000);seconds++;if(timed&&seconds>=limit)finish()}}
 LaunchedEffect(typed,ended){if(!timed&&typed.length>=target.length&&!ended)finish()}
 BackHandler{exit()}
 Column(Modifier.fillMaxSize().padding(16.dp).focusRequester(focus).onPreviewKeyEvent{e->
  if(e.type!=KeyEventType.KeyDown||paused||ended)return@onPreviewKeyEvent false
  when(e.key){
   Key.Backspace->{if(typed.isNotEmpty())typed=typed.dropLast(1);true}
   Key.Escape->{exit();true}
   Key.Spacebar->{if(typed.length<target.length){if(target[typed.length]!=' ')errors++;typed+=' '};true}
   Key.Enter->{if(typed.length<target.length){if(target[typed.length]!='\n')errors++;typed+='\n'};true}
   else->{val code=e.nativeKeyEvent.unicodeChar;if(code!=0&&!e.nativeKeyEvent.isCtrlPressed&&!e.nativeKeyEvent.isAltPressed&&typed.length<target.length){val ch=code.toChar();if(target[typed.length]!=ch)errors++;typed+=ch;true}else false}
  }
 }.focusable(),verticalArrangement=Arrangement.spacedBy(12.dp)){
  Row(verticalAlignment=Alignment.CenterVertically){
   TextButton(onClick=exit){Text("← Arcade")}
   Column(Modifier.weight(1f)){Text(mode,fontWeight=FontWeight.Bold);Text(if(timed)"$limit-second challenge" else "Complete the full challenge",color=Color.Gray,fontSize=11.sp)}
   Text(if(timed)(limit-seconds).coerceAtLeast(0).toString()+"s" else "%02d:%02d".format(seconds/60,seconds%60),fontWeight=FontWeight.Bold)
  }
  Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
   ArcadeMetric("WPM",wpm.toString(),Modifier.weight(1f));ArcadeMetric("Accuracy","$accuracy%",Modifier.weight(1f));ArcadeMetric("Errors",errors.toString(),Modifier.weight(1f))
  }
  LinearProgressIndicator(progress={if(timed)(seconds.toFloat()/limit).coerceIn(0f,1f) else (typed.length.toFloat()/target.length).coerceIn(0f,1f)},modifier=Modifier.fillMaxWidth())
  Card(Modifier.weight(1f).fillMaxWidth(),shape=RoundedCornerShape(18.dp)){
   Column(Modifier.fillMaxSize().padding(18.dp),verticalArrangement=Arrangement.Center){
    Text("TYPE THE CHALLENGE",color=Color.Gray,fontSize=10.sp,fontWeight=FontWeight.Bold);Spacer(Modifier.height(12.dp))
    Text(buildAnnotatedString{target.forEachIndexed{i,ch->val color=when{ i<typed.length&&typed[i]==ch->Color(0xFF218653);i<typed.length->Color(0xFFD34A4A);i==typed.length->Color(0xFF4169E1);else->Color(0xFF778394)};withStyle(SpanStyle(color=color,fontWeight=if(i==typed.length)FontWeight.Bold else FontWeight.Normal)){append(ch)}}},fontSize=21.sp,lineHeight=32.sp)
   }
  }
  Text("Connect your USB OTG or Bluetooth keyboard and type. Backspace corrects the previous character.",color=Color.Gray,fontSize=11.sp)
  Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
   OutlinedButton(onClick={paused=!paused},modifier=Modifier.weight(1f)){Text(if(paused)"Resume" else "Pause")}
   OutlinedButton(onClick={typed="";errors=0;seconds=0;paused=false;ended=false},modifier=Modifier.weight(1f)){Text("Restart")}
  }
  if(paused)Text("Paused",color=Color(0xFF4169E1),fontWeight=FontWeight.Bold)
  LaunchedEffect(Unit){focus.requestFocus()}
 }
}
@Composable private fun ArcadeMetric(label:String,value:String,modifier:Modifier=Modifier){
 Card(modifier,shape=RoundedCornerShape(12.dp)){Column(Modifier.padding(10.dp)){Text(label,color=Color.Gray,fontSize=10.sp);Text(value,fontWeight=FontWeight.Bold,fontSize=18.sp)}}
}
