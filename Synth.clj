(import '(javax.sound.midi MidiSystem) )
(def synth (MidiSystem/getSynthesizer))
(.open synth)

(def instruments (.getAvailableInstruments synth))

(dotimes [i (count instruments)]
	(println (str i (nth instruments i)))
)

; See what instruments are loaded with --
; (map (fn [a] (.getName a)) instruments)
; (.loadInstrument synth (nth instruments 3))

; num, for most implementations should be 0 through 15.
(defn channel[num]
	(nth (.getChannels synth) num)
)

(defn playNote[channel note intensity duration]
	(.start (new Thread (fn []
		(.noteOn channel note intensity)
		(Thread/sleep duration)
		(.noteOff channel note)
	)))
)

; NOTES-- 
; 60 is C4
; 61 - C4 #
; 62 - D4
; 63 - D4 #
; 64 - E4
; 65 - F4
; 66 - F4 #
; 67 - G4
; 68 - G4 #
; 69 - A4
; 70 - A4 #
; 71 - B4
; 72 - C5
; etc

; Music is (list
;  [note1 intensity1 duration1 pauseAfter1]
;  [note2 intensity2 duration2 pauseAfter2] ... )
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
	(playNotes (channel 0) (list 
		[51 155 100 0] [53 155 100 300] 
		[51 155 100 0] [53 155 100 300]
		[55 155 100 0] [59 155 100 250]
		[55 155 100 0] [57 155 100 300]
		[48 155 200 0] [48 155 200 100]
	))
)
(beat)

(defn randProgShift[]
	(.programChange (channel 0) (rand-int 120))
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

(playNotes (channel 1)
	(list
		[62 255 300 300]
		[64 255 300 300]
		[63 255 300 250]
		[62 255 300 300]
		[62 255 300 100]
		[60 255 300 300]
		[61 255 300 300]
		[60 255 300 250]
		[60 255 300 300]
	)
)

(def isItOn (new Integer 4))
(def isItOn nil)
(startBeat beat 0)



(.close synth)
