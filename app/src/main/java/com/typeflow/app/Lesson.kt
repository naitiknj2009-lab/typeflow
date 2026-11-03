package com.typeflow.app

data class Lesson(val id:Int,val title:String,val course:String,val objective:String,val prompt:String,val level:String,val fingerTip:String,val keys:String)
data class Stage(val range:IntRange,val title:String,val objective:String)

object Curriculum {
 val stages=listOf(
  Stage(1..5,"Getting Started","Learn posture, home position, and the F and J guide keys."),
  Stage(6..20,"Home Row","Build accuracy with A S D F and J K L ;."),
  Stage(21..40,"Top Row","Reach Q W E R and U I O P, then return home."),
  Stage(41..55,"Bottom Row","Reach Z X C V and N M , . with relaxed hands."),
  Stage(56..73,"All Letter Rows","Combine all letters into common words and sentences."),
  Stage(74..83,"Numbers & Capitals","Use Shift and the number row with the opposite hand."),
  Stage(84..93,"Punctuation","Type punctuation, quotation marks, and complete sentences."),
  Stage(94..110,"Everyday Writing","Practise useful messages, notes, and paragraphs."),
  Stage(111..126,"Advanced Practice","Handle mixed text, longer passages, and varied key patterns.")
 )
 private val prompts=listOf(
 "asdf jkl; asdf jkl;","a s d f j k l ;","fff jjj ddd kkk sss lll aaa ;;;","sad dad ask fall flask","a dad asks; a lad falls.","all fall; ask dad; add a salad.","ask a lad; a flask; a salad; all lads.","dad adds a salad; a lad asks dad.","fall, ask, add, flask, sad.","a; s; d; f; j; k; l; ;",
 "a sad lad asks dad.","all lads add a flask.","a flask falls; dad asks.","j; k; l; a; s; d; f;","salad is a safe dish; add salt.","a lad had a fall; dad asks if all is well.","ask dad; add a dash; fall back.","half a flask; a sad dad; a glad lad.","a small salad falls; all ask dad.","dad has a flask and a salad.",
 "qwer uiop qwer uiop","q w e r u i o p","were ripe power quiet","write your own quiet poem","our upper row is ready.","we were quiet; you were proud.","power your ideas with practice.","type with your left and right hands.","we write, you read, and we improve.","quite a few people write well.","quiet people write powerful stories.","we require quite a few new ideas.","your work will improve with practice.","write a poem; read it aloud.","we were never quite ready to quit.","our team will write a review.","you are required to wear a warm coat.","powerful ideas grow when we write.","we use the upper row with care.","the quick brown fox jumps over the lazy dog.",
 "a clever writer explores new ideas.","vivid caves contain many maps.","can mix, move, and zoom.","z x c v n m , .","move the van; come by noon.","zoom past the next curve.","many small moves make a difference.","come, move, and make a new plan.","calm minds make fewer mistakes.","the cave is calm and the view is vivid.","move the camera; zoom in carefully.","many curious minds can solve this.","never move your hands too far.","mix, move, zoom, and come back.","a calm mind can make a clever move.",
 "small steps make steady progress.","practice builds skill and confidence.","steady hands create a steady rhythm.","accuracy comes before speed.","small steps lead to meaningful progress.","keep your eyes on the screen.","let your fingers find each key.","good habits make typing feel natural.","correct mistakes and keep moving forward.","build a rhythm that feels relaxed and smooth.",
 "every expert started as a beginner.","focus on the next character, not the clock.","relaxed shoulders help your hands move freely.","accuracy is the foundation of fluent typing.","a steady rhythm is better than a rushed start.","look at the screen and trust your fingers.","learn the pattern, practise the pattern, then use it.","take a short pause when your hands feel tired.",
 "123 456 7890","2026 1024 2048 365","Room 12 has 24 chairs.","There are 7 days in a week.","Version 2.5 is ready!","I scored 95% on the test.","The total is 48 + 27 = 75.","Use 10, 20, and 30 as examples.","Price: $25.50 (save 10%).","The train leaves at 6:45 on platform 3.",
 "Hello, world! How are you today?","Typing is fun; practice makes it better.","Wait... is that correct? Yes, it is.","She said, \"Keep trying!\"","First: focus. Second: practise. Third: improve.","Can you type this sentence?","One step at a time — that is the way.","It's a bright, beautiful day.","(Open the page) [read the note] {finish the task}.","Well-written work is clear, calm, and precise.",
 "The quick brown fox jumps over the lazy dog.","A good message is clear, polite, and easy to read.","Please review the notes before the meeting begins.","Learning a skill takes patience, practice, and curiosity.","Save your work, check the details, and share it safely.","A helpful plan turns a large task into small steps.","Read carefully, think clearly, and explain your ideas.","Good communication starts with thoughtful words.","The library is quiet, bright, and full of useful books.","Every day offers another chance to learn something new.",
 "Typing smoothly is about control, comfort, and consistency.","A careful writer checks spelling before sending a message.","The best pace is one that lets you stay accurate.","Keep your wrists relaxed and your fingers gently curved.","A strong foundation makes advanced skills easier to learn.","When you make a mistake, correct it and continue calmly.","Use both hands evenly and return to the home row.","Clear writing helps people understand complicated ideas.","A little focused practice can make a noticeable difference.","Type the words you see; do not guess what comes next.",
 "Today I will practise with patience and steady attention.","Numbers, symbols, and punctuation all deserve careful practice.","The goal is confident, comfortable, accurate typing.","Pack my box with five dozen liquor jugs.","Sphinx of black quartz, judge my vow.","Email: hello@example.com | Status: 100% complete.","Order #4821 costs $39.95; delivery is due on 11/12/2026.","Use Ctrl+C to copy, Ctrl+V to paste, and Ctrl+Z to undo.","The meeting starts at 09:30; please arrive five minutes early.","Mix letters, numbers (42), symbols (&), and punctuation!","A curious learner asks questions, tests ideas, and keeps improving.","Write with care: clarity matters more than unnecessary complexity.","Final challenge: stay relaxed, accurate, and consistent from start to finish."
 )
 private fun tip(id:Int)=when(id){
  in 1..5->"Sit comfortably. Keep wrists straight, shoulders relaxed, and both index fingers on the F and J bumps."
  in 6..20->"Left little finger: A; ring: S; middle: D; index: F. Right index: J; middle: K; ring: L; little: semicolon. Thumbs press Space."
  in 21..35->"Reach up from home row: left little Q, ring W, middle E, index R/T; right index Y/U, middle I, ring O, little P. Return home."
  in 36..50->"Reach down gently: left little Z, ring X, middle C, index V/B; right index N/M, middle comma, ring period, little slash. Return home."
  in 74..83->"For capitals, hold Shift with the opposite hand. Use the same finger zones for the number row; do not stretch your wrist."
  else->"Use the finger nearest each key, keep wrists neutral, and return to the home row after reaches. Look at the screen rather than your hands."
 }
 private fun keys(id:Int)=when(id){in 1..20->"A S D F  •  J K L ;";in 21..35->"Q W E R T  •  Y U I O P";in 36..50->"Z X C V B  •  N M , . /";in 61..70->"Shift + number row";in 71..80->"Comma • period • quotes • symbols";else->"Full keyboard"}
 val lessons:List<Lesson> = prompts.mapIndexed { i,p ->
  val id=i+1; val s=stages.first{ id in it.range }
  Lesson(id,"Lesson %03d".format(id),s.title,s.objective,p,when(id){in 1..20->"Beginner";in 21..83->"Intermediate";else->"Advanced"},tip(id),keys(id))
 }
}
