package com.krist.train.domain.prompt

object TrainingPlanJsonSchema {
    val schemaExample = """
        {
          "overview": "Race day is 14 weeks away. This aggressive running-only plan builds threshold strength first, then race-specific endurance and sharpening to reach the target time.",
          "assumptions": ["Estimated threshold pace is 4:35/km and is used to set workout paces."],
          "weeks": [
            {
              "week": 1,
              "focus": "Build volume and introduce race-specific speed",
              "workouts": [
                {
                  "day": "Tuesday",
                  "type": "Intervals",
                  "title": "4 x 4 min, pace: 3:40/km",
                  "body": "High-intensity intervals in Olympiatoppen I-4/I-5. Run 2 km warmup, then 4 x 4 min at 3:40/km with 2 min easy jog recoveries, then 2 km cooldown. Total distance about 10.0 km. Estimated duration: 48-52 min.",
                  "heartZone": "I-4/I-5",
                  "details": "Distance: 10.0 km. Pace: 3:40/km for reps, 5:20-5:45/km warmup/cooldown. Estimated duration: 48-52 min. Structure: 2 km warmup, 4 x 4 min at 3:40/km with 2 min jog recovery, 2 km cooldown.",
                  "purpose": "Improve speed and VO2max while supporting the goal pace."
                },
                {
                  "day": "Thursday",
                  "type": "Threshold",
                  "title": "3 x 10 min, pace: 4:00/km",
                  "body": "Controlled threshold work in Olympiatoppen I-4. Run 2 km warmup, 3 x 10 min at 4:00/km with 3 min jog recovery, and 2 km cooldown. Total distance about 11.0 km. Estimated duration: 52-56 min.",
                  "heartZone": "I-4",
                  "details": "Distance: 11.0 km. Pace: 4:00/km for threshold reps, 5:20-5:45/km warmup/cooldown. Estimated duration: 52-56 min. Structure: 2 km warmup, 3 x 10 min at 4:00/km with 3 min jog recovery, 2 km cooldown.",
                  "purpose": "Raise threshold speed and make goal pace feel controlled."
                },
                {
                  "day": "Sunday",
                  "type": "Long run",
                  "title": "16 km, pace: 5:10-5:35/km",
                  "body": "Controlled aerobic long run in Olympiatoppen I-1/I-2. Keep it relaxed and avoid drifting into threshold effort. Estimated duration: 83-89 min.",
                  "heartZone": "I-1/I-2",
                  "details": "Distance: 16.0 km. Pace: 5:10-5:35/km. Estimated duration: 83-89 min.",
                  "purpose": "Build endurance for the target race distance."
                }
              ]
            }
          ]
        }
    """.trimIndent()
}
