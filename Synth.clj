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
(defn playNote[channel note duration]
	(.start (new Thread (fn []
		(.noteOn channel note 200)
		(Thread/sleep duration)
		(.noteOff channel note)
	)))
)






(count (list "a" "b" "c"))

(defn beat[]
	(.noteOn channel 65 200)
	(.noteOn channel 67 200)
	(Thread/sleep 150)
	(.noteOff channel 65)	
	(.noteOff channel 67)	
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
(startBeat beat 375)



(.close synth)








