(import '(javax.sound.midi MidiSystem ShortMessage) )
(def synth (MidiSystem/getSynthesizer))
(.open synth)

(def instruments (.getAvailableInstruments synth))


; See what instruments are loaded with --
; (map (fn [a] (.getName a)) instruments)

(.loadInstrument synth (nth instruments 3))

(def channel (nth (.getChannels synth) 0))

;(defn programChange [chan instrument]
;	(let [q (new ShortMessage)]
;		(.setMessage q (ShortMessage/PROGRAM_CHANGE 0 instrument 0) 
;	)
;)

(.programChange channel (rand-int 120))



; Channel's where the real magic is. There should be 16 of 'dem.
(.noteOn channel 65 200)
(.noteOff channel 65)

; This'll probably make it into the final run
(defn playNote[channel note intensity duration]
	(.start (new Thread (fn []
		(.noteOn channel note intensity)
		(Thread/sleep duration)
		(.noteOff channel note)
	)))
)

; Guessin' music is gonna be some something like
;  [note1 intensity1 duration1 pauseAfter1]
;  [note2 intensity2 duration2 pauseAfter2] ... 
(defn playNotes [channel music]
	(dotimes [n (count music)]
		(let [q (nth music n)]
			(playNote channel (nth q 0) (nth q 1) (nth q 2))
			(Thread/sleep (nth q 3))
		)
	)
)




(defn beat[]
	; Use zero to play two notes simultaneously...
	(playNotes channel (list 
		[65 255 100 0] [67 255 100 300] 
		[65 255 100 0] [67 255 100 300] 
		[67 255 100 0] [71 255 100 250]
		[67 255 100 0] [69 255 100 300]
	))
)

(beat)

(defn randProgShift[]
	(.programChange channel (rand-int 120))
	(beat)
)
(randProgShift)


(defn startBeat[f millis]
	(.start (new Thread (fn []
	(while (not (nil? isItOn))
		(eval (list f))
		(Thread/sleep millis)
	))))
)

(def isItOn (new Integer 4))
(def isItOn nil)
(startBeat beat 0)



(.close synth)








