package com.typeflow.app
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
private val Blue=Color(0xFF4169E1);private val Ink=Color(0xFF172433);private val Muted=Color(0xFF778394);private val Green=Color(0xFF218653)
class MainActivity:ComponentActivity(){override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);setContent{TypeFlowApp()}}}
@Composable private fun TypeFlowApp(){
 val c=LocalContext.current;val pref=remember{c.getSharedPreferences("typeflow_progress",0)}
 var done by remember{mutableIntStateOf(pref.getInt("done",0))};var best by remember{mutableIntStateOf(pref.getInt("best",0))};var acc by remember{mutableIntStateOf(pref.getInt("acc",100))};var sessions by remember{mutableIntStateOf(pref.getInt("sessions",0))};var games by remember{mutableIntStateOf(pref.getInt("games",0))};var dark by remember{mutableStateOf(pref.getBoolean("dark",false))}
 var page by remember{mutableStateOf("Home")};var lesson by remember{mutableStateOf<Lesson?>(null)};var game by remember{mutableStateOf<String?>(null)}
 fun save(r:TypingResult,id:Int?=null,g:Boolean=false){if(id!=null)done=maxOf(done,id);best=maxOf(best,r.wpm);acc=r.accuracy;sessions++;if(g)games++;pref.edit().putInt("done",done).putInt("best",best).putInt("acc",acc).putInt("sessions",sessions).putInt("games",games).apply()}
 val scheme=if(dark)darkColorScheme(primary=Color(0xFF91A9FF),surface=Color(0xFF1B2938))else lightColorScheme(primary=Blue,surface=Color.White,onSurface=Ink)
 MaterialTheme(colorScheme=scheme){Surface(Modifier.fillMaxSize(),color=if(dark)Color(0xFF101923)else Color(0xFFF4F6FA)){when{
 lesson!=null->Session(lesson!!.title,lesson!!.course,lesson!!.objective,lesson!!.prompt,lesson!!.fingerTip,lesson!!.keys,{lesson=null}){save(it,lesson!!.id);lesson=null;page="Progress"}
 game!=null->{val g=game!!;ArcadeSession(g,{game=null}){save(it,g=true);game=null;page="Progress"}}
 else->Column(Modifier.fillMaxSize()){
 Row(Modifier.fillMaxWidth().padding(20.dp),verticalAlignment=Alignment.CenterVertically){Text("Type",fontSize=25.sp,fontWeight=FontWeight.Black);Text("Flow",fontSize=25.sp,fontWeight=FontWeight.Black,color=Blue);Spacer(Modifier.weight(1f));Text("OFFLINE",color=Muted,fontSize=10.sp)}
 AnimatedContent(page,label="page",modifier=Modifier.weight(1f)){p->when(p){"Home"->Home(done,best,acc,sessions,{lesson=Curriculum.lessons[done.coerceIn(0,109)]},{page="Lessons"},{page="Arcade"});"Lessons"->Lessons(done){lesson=it};"Arcade"->Arcade{game=it};"Progress"->Progress(done,best,acc,sessions,games);else->Settings(dark,{dark=it;pref.edit().putBoolean("dark",it).apply()},{pref.edit().clear().apply();done=0;best=0;acc=100;sessions=0;games=0})}}
 NavigationBar{listOf("Home","Lessons","Arcade","Progress","Settings").forEach{t->NavigationBarItem(page==t,{page=t},icon={Text(when(t){"Home"->"⌂";"Lessons"->"⌨";"Arcade"->"✦";"Progress"->"↗";else->"⚙"})},label={Text(t,fontSize=10.sp)})}}
 }
 }}}
}
@Composable private fun Metric(a:String,b:String,m:Modifier=Modifier){Card(m,shape=RoundedCornerShape(14.dp)){Column(Modifier.padding(12.dp)){Text(a,color=Muted,fontSize=10.sp);Text(b,fontSize=19.sp,fontWeight=FontWeight.Bold)}}}
@Composable private fun Home(done:Int,best:Int,acc:Int,sessions:Int,go:()->Unit,learn:()->Unit,arcade:()->Unit){LazyColumn(contentPadding=PaddingValues(18.dp),verticalArrangement=Arrangement.spacedBy(14.dp)){item{Text("Your typing journey",fontSize=27.sp,fontWeight=FontWeight.Bold);Text("Learn the right technique. Let speed follow.",color=Muted)};item{Card(colors=CardDefaults.cardColors(containerColor=Ink),shape=RoundedCornerShape(20.dp)){Column(Modifier.padding(20.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){Text("YOUR NEXT LESSON",color=Color(0xFFB9C9FF),fontSize=10.sp);Text(if(done==0)"Start with the home row" else "Keep your momentum going",color=Color.White,fontSize=22.sp,fontWeight=FontWeight.Bold);Text("127 guided lessons from first finger placement to confident typing.",color=Color.LightGray);Button(onClick=go){Text("Continue learning →")}}}};item{Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){Metric("Lessons","$done/127",Modifier.weight(1f));Metric("Best WPM","$best",Modifier.weight(1f));Metric("Accuracy","$acc%",Modifier.weight(1f))}};item{Card(Modifier.fillMaxWidth().clickable{arcade()},shape=RoundedCornerShape(15.dp)){Row(Modifier.padding(16.dp),verticalAlignment=Alignment.CenterVertically){Text("✦",fontSize=28.sp,color=Blue);Column(Modifier.weight(1f).padding(start=10.dp)){Text("TypeFlow Arcade",fontWeight=FontWeight.Bold);Text("Quick challenges between lessons",color=Muted,fontSize=12.sp)};Text("Play →",color=Blue)}}};item{Text("$sessions practice sessions completed.",color=Muted);OutlinedButton(onClick=learn,modifier=Modifier.fillMaxWidth()){Text("Browse all 127 lessons")};Text("No account • Offline • Progress saved on this device",color=Muted,fontSize=11.sp,textAlign=TextAlign.Center,modifier=Modifier.fillMaxWidth())}}}
@Composable private fun Lessons(done:Int,start:(Lesson)->Unit){LazyColumn(contentPadding=PaddingValues(18.dp),verticalArrangement=Arrangement.spacedBy(8.dp)){item{Text("Learning path",fontSize=27.sp,fontWeight=FontWeight.Bold);Text("127 lessons • 10 skill stages",color=Muted)};items(Curriculum.lessons){l->val open=l.id<=done+1;Card(Modifier.fillMaxWidth().clickable(enabled=open){start(l)},shape=RoundedCornerShape(13.dp)){Row(Modifier.padding(13.dp),verticalAlignment=Alignment.CenterVertically){Text(if(l.id<=done)"✓" else if(open)"▶" else "•",color=Blue);Column(Modifier.weight(1f).padding(horizontal=12.dp)){Text(l.title+" · "+l.course,fontWeight=FontWeight.SemiBold,fontSize=13.sp);Text(l.objective,color=Muted,fontSize=11.sp)};Text(if(open)"Start" else "Locked",color=if(open)Blue else Muted,fontSize=11.sp)}}}}}
@Composable private fun Arcade(start:(String)->Unit){LazyColumn(contentPadding=PaddingValues(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){item{Text("TypeFlow Arcade",fontSize=27.sp,fontWeight=FontWeight.Bold);Text("Short challenges to make practice fun.",color=Muted)};items(listOf("Word Sprint","Sentence Flow","Accuracy Quest","Number & Symbol Mix")){name->Card(Modifier.fillMaxWidth().clickable{start(name)},shape=RoundedCornerShape(15.dp)){Row(Modifier.padding(17.dp),verticalAlignment=Alignment.CenterVertically){Text("✦",fontSize=25.sp,color=Blue);Text(name,Modifier.weight(1f).padding(start=12.dp),fontWeight=FontWeight.Bold);Text("Play →",color=Blue)}}}}}
@Composable private fun Progress(done:Int,best:Int,acc:Int,sessions:Int,games:Int){LazyColumn(contentPadding=PaddingValues(20.dp),verticalArrangement=Arrangement.spacedBy(13.dp)){item{Text("Your progress",fontSize=28.sp,fontWeight=FontWeight.Bold)};item{Text("Course completion  "+(done*100/127)+"%",fontWeight=FontWeight.Bold);LinearProgressIndicator(progress={done/127f},modifier=Modifier.fillMaxWidth());Text(done.toString()+" of 127 lessons completed",color=Muted)};item{Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){Metric("Best speed",best.toString()+" WPM",Modifier.weight(1f));Metric("Last accuracy",acc.toString()+"%",Modifier.weight(1f))}};item{Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){Metric("Sessions",sessions.toString(),Modifier.weight(1f));Metric("Games played",games.toString(),Modifier.weight(1f))}}}}
@Composable private fun Settings(dark:Boolean,onDark:(Boolean)->Unit,reset:()->Unit){var ask by remember{mutableStateOf(false)};Column(Modifier.fillMaxSize().padding(20.dp),verticalArrangement=Arrangement.spacedBy(15.dp)){Text("Settings",fontSize=28.sp,fontWeight=FontWeight.Bold);Card{Column(Modifier.padding(16.dp)){Row(verticalAlignment=Alignment.CenterVertically){Text("Dark appearance",Modifier.weight(1f));Switch(dark,onDark)};HorizontalDivider();Text("USB OTG & Bluetooth keyboards are supported. Tap a lesson and type directly. Backspace corrects; Esc exits.",color=Muted);Text("Home position: left fingers on A S D F, right fingers on J K L ;, thumbs near Space.",color=Muted,modifier=Modifier.padding(top=12.dp))}};OutlinedButton(onClick={ask=true},modifier=Modifier.fillMaxWidth()){Text("Reset all progress")};Text("TypeFlow • English • Android 10+ • Offline",color=Muted,fontSize=12.sp)};if(ask)AlertDialog(onDismissRequest={ask=false},title={Text("Reset progress?")},text={Text("This clears all locally saved progress.")},confirmButton={TextButton(onClick={reset;ask=false}){Text("Reset")}},dismissButton={TextButton(onClick={ask=false}){Text("Cancel")}})}
@Composable private fun Session(title:String,sub:String,objective:String,prompt:String,tip:String,keys:String,exit:()->Unit,complete:(TypingResult)->Unit){
 val engine=remember(prompt){TypingEngine(prompt)};var typed by remember(prompt){mutableStateOf("")};var secs by remember(prompt){mutableIntStateOf(0)};var paused by remember(prompt){mutableStateOf(false)};var finished by remember(prompt){mutableStateOf(false)};val focus=remember{FocusRequester()}
 LaunchedEffect(paused,finished){while(!paused&&!finished){delay(1000);secs++}}
 LaunchedEffect(engine.isComplete){if(engine.isComplete&&!finished){finished=true;delay(200);complete(engine.result(secs))}}
 BackHandler{exit()}
 Column(Modifier.fillMaxSize().padding(15.dp).focusRequester(focus).onPreviewKeyEvent{e->if(e.type!=KeyEventType.KeyDown||paused||finished)return@onPreviewKeyEvent false;when(e.key){Key.Backspace->{engine.backspace();typed=engine.input;true};Key.Enter->{engine.accept('\n');typed=engine.input;true};Key.Spacebar->{engine.accept(' ');typed=engine.input;true};Key.Escape->{exit();true};else->{val ch=e.nativeKeyEvent.unicodeChar;if(ch!=0&&!e.nativeKeyEvent.isCtrlPressed&&!e.nativeKeyEvent.isAltPressed){engine.accept(ch.toChar());typed=engine.input;true}else false}}}.focusable(),verticalArrangement=Arrangement.spacedBy(10.dp)){
 Row(verticalAlignment=Alignment.CenterVertically){TextButton(onClick=exit){Text("← Exit")};Column(Modifier.weight(1f)){Text(title,fontWeight=FontWeight.Bold);Text(sub,color=Muted,fontSize=11.sp)};Text("%02d:%02d".format(secs/60,secs%60),fontWeight=FontWeight.Bold)}
 Text(objective,color=Muted,fontSize=12.sp);Row(horizontalArrangement=Arrangement.spacedBy(7.dp)){Metric("WPM",if(secs==0)"0" else ((engine.correctCount()/5f)/(secs/60f)).toInt().toString(),Modifier.weight(1f));Metric("Accuracy",if(typed.isEmpty())"100%" else (engine.correctCount()*100/typed.length).toString()+"%",Modifier.weight(1f));Metric("Errors",engine.errors.toString(),Modifier.weight(1f))}
 Card(Modifier.weight(1f).fillMaxWidth(),shape=RoundedCornerShape(18.dp)){Column(Modifier.fillMaxSize().padding(17.dp),verticalArrangement=Arrangement.Center){Text("TYPE THE TEXT BELOW",color=Muted,fontSize=10.sp,fontWeight=FontWeight.Bold);Spacer(Modifier.height(14.dp));Text(buildAnnotatedString{prompt.forEachIndexed{i,ch->val col=when{ i<typed.length&&typed[i]==ch->Green;i<typed.length->Color.Red;i==typed.length->Blue;else->Ink.copy(alpha=.5f)};withStyle(SpanStyle(color=col,fontWeight=if(i==typed.length)FontWeight.Bold else FontWeight.Normal)){append(ch)}}},fontSize=22.sp,lineHeight=35.sp);Text("Typed: "+typed,color=Muted,fontSize=12.sp,modifier=Modifier.padding(top=12.dp))}}
 Text("Keys: "+keys,color=Blue,fontSize=11.sp);KeyboardGuide();Card{Column(Modifier.padding(11.dp)){Text("FINGER TIP",color=Blue,fontWeight=FontWeight.Bold,fontSize=10.sp);Text(tip,color=Muted,fontSize=12.sp)}};Text(if(typed.isEmpty())"Connect USB OTG or Bluetooth keyboard and start typing." else typed.length.toString()+" / "+prompt.length+" characters",color=Muted,fontSize=11.sp)
 Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){OutlinedButton(onClick={paused=!paused},modifier=Modifier.weight(1f)){Text(if(paused)"Resume" else "Pause")};OutlinedButton(onClick={engine.reset();typed="";secs=0;paused=false;finished=false},modifier=Modifier.weight(1f)){Text("Restart")}}
 LaunchedEffect(Unit){focus.requestFocus()}
 }
}