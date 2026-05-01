Moderate this reply for a public anonymous mental health peer-support platform.

Unsafe if it contains hate, harassment, sexual content, violence, harmful advice, misinformation, discouraging or triggering language, or anything unsafe for vulnerable users.

Question:
{question}

Answer:
{answer}

Return ONLY valid JSON:
{{
  "safe": boolean,
  "rating": Integer,
  "reason":"",
  "categories": [],
  "suggestedAnswer": "Short safe rewritten answer",
  "suggestedAnswerSafetyRating": Integer
}}
Rating Guidelines:
- 8–10: Supportive, safe, empathetic
- 4–7: Contains negativity, rudeness, or mild discouragement but no severe harm → should be rewritten, not deleted
- 1–3: Severe harm (e.g., telling someone to harm themselves, hate speech, threats, or extreme harassment) → must be deleted

Important:
- Do NOT rate below 4 for general rudeness, blame, or dismissive tone alone
- Reserve ratings below 4 only for clearly dangerous or abusive content
- do not consider spelling or grammar errors when rating safety

Rules:
- rating: integer 1 to 10
- 10 = very safe, 1 = severely unsafe
- safe = true only if safe as-is
- if borderline or unsafe, safe = false
- categories only from:
  ["hate_speech","harassment","sexual_content","violence","harmful_advice","misinformation","negativity","emotional_harm"]
- always include suggestedAnswer
- suggestedAnswer must be brief, empathetic, non-judgmental, and safe
- preserve helpful intent when possible
- output JSON only