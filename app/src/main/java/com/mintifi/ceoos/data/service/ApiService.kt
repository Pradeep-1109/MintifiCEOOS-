package com.mintifi.ceoos.data.service

import android.content.Context
import com.mintifi.ceoos.data.model.Category
import com.mintifi.ceoos.data.model.Priority
import com.mintifi.ceoos.data.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.concurrent.TimeUnit

class ApiService(private val context: Context) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private fun prefs() = context.getSharedPreferences("ceo_os_prefs", Context.MODE_PRIVATE)
    private fun geminiKey()    = prefs().getString("gemini_key", "") ?: ""
    private fun groqKey()      = prefs().getString("groq_key", "") ?: ""
    private fun anthropicKey() = prefs().getString("anthropic_key", "") ?: ""
    private fun deepgramKey()  = prefs().getString("deepgram_key", "") ?: ""

    suspend fun transcribeAudio(file: File): String = withContext(Dispatchers.IO) {
        val groq = groqKey()
        if (groq.isNotBlank()) return@withContext transcribeGroq(file, groq)
        val dg = deepgramKey()
        if (dg.isNotBlank()) return@withContext transcribeDeepgram(file, dg)
        throw Exception("No transcription key. Add Groq key (free) in Settings.")
    }

    private fun transcribeGroq(file: File, key: String): String {
        val body = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("file", file.name, RequestBody.create("audio/m4a".toMediaType(), file))
            .addFormDataPart("model", "whisper-large-v3")
            .addFormDataPart("response_format", "verbose_json")
            .build()
        val req = Request.Builder()
            .url("https://api.groq.com/openai/v1/audio/transcriptions")
            .header("Authorization", "Bearer " + key)
            .post(body).build()
        return JSONObject(client.newCall(req).execute().body!!.string()).optString("text", "")
    }

    private fun transcribeDeepgram(file: File, key: String): String {
        val req = Request.Builder()
            .url("https://api.deepgram.com/v1/listen?model=nova-2&language=hi-en&punctuate=true&diarize=true&detect_language=true")
            .header("Authorization", "Token " + key)
            .post(RequestBody.create("audio/m4a".toMediaType(), file)).build()
        val json = JSONObject(client.newCall(req).execute().body!!.string())
        return json.getJSONObject("results").getJSONArray("channels")
            .getJSONObject(0).getJSONArray("alternatives")
            .getJSONObject(0).optString("transcript", "")
    }

    suspend fun generateSummary(transcript: String): String = withContext(Dispatchers.IO) {
        val sections = "OVERVIEW: (2-3 sentences) | PROJECTS DISCUSSED: (bullet list) | KEY DECISIONS: (bullet list) | NEXT STEPS: (bullet list)"
        val prompt = "You are analyzing a CEO meeting for Pradeep Singh at Mintifi Finserv. " +
            "The text may be in English, Hindi, or Hinglish. Respond in English only. " +
            "Generate a structured meeting summary with these sections: " + sections + ". " +
            "Transcript: " + transcript
        callBestAI(prompt)
    }

    suspend fun analyzeTranscript(transcript: String, ctx: String = ""): List<Task> =
        withContext(Dispatchers.IO) {
            val fields = "title (English), detail (English), priority (P0/P1/P2/P3), " +
                "category (CEO_ASK/DDR/LAP/COLLECTIONS/AI_PROJECTS/TREDS/MBA/GENERAL), " +
                "owner, deadline, projectGroup (project or topic name)"
            val prompt = "Analyze this CEO meeting for Pradeep Singh at Mintifi Finserv. " +
                "The transcript may be in English, Hindi, or Hinglish - understand all. " +
                "Extract ALL action items. Group related tasks under the same projectGroup. " +
                "Return ONLY a JSON array. Each item must have: " + fields + ". " +
                "Context: " + ctx + " Transcript: " + transcript
            parseTasksFromResponse(callBestAI(prompt))
        }

    suspend fun generateBriefing(tasks: List<Task>): String = withContext(Dispatchers.IO) {
        val lines = tasks.take(20).joinToString(" | ") { t ->
            "[" + t.priority.label + "] " + t.title
        }
        val prompt = "You are Pradeep Singh AI Chief of Staff at Mintifi Finserv. " +
            "Generate a sharp executive morning briefing for these tasks: " + lines + ". " +
            "Format: 2-3 sentences on priorities then 2-3 action items. Be concise."
        callBestAI(prompt)
    }

    suspend fun chatWithCopilot(
        messages: List<Pair<String, String>>,
        taskContext: String
    ): String = withContext(Dispatchers.IO) {
        val system = "You are the AI Chief of Staff for Pradeep Singh at Mintifi Finserv. " +
            "Mintifi does supply chain finance, lending, LAP, DDR automation, TReDS, collections. " +
            "You understand Hindi, English, and Hinglish. " +
            "Current tasks: " + taskContext + " Be concise and actionable."
        callBestAIWithHistory(system, messages)
    }

    private fun callBestAI(prompt: String): String {
        val gemini = geminiKey()
        if (gemini.isNotBlank()) runCatching { return callGemini(prompt, gemini) }
        val groq = groqKey()
        if (groq.isNotBlank()) runCatching { return callGroq(prompt, groq) }
        val anthropic = anthropicKey()
        if (anthropic.isNotBlank()) runCatching { return callAnthropic(prompt, anthropic) }
        return "No AI key configured. Add Gemini key (free at ai.google.dev) in Settings."
    }

    private fun callBestAIWithHistory(system: String, messages: List<Pair<String, String>>): String {
        val groq = groqKey()
        if (groq.isNotBlank()) runCatching { return callGroqHistory(system, messages, groq) }
        val gemini = geminiKey()
        if (gemini.isNotBlank()) {
            val combined = system + " " + messages.joinToString(" ") { m -> m.first + ": " + m.second }
            runCatching { return callGemini(combined, gemini) }
        }
        val anthropic = anthropicKey()
        if (anthropic.isNotBlank()) runCatching { return callAnthropicHistory(system, messages, anthropic) }
        return "No AI key configured. Add a free Groq key in Settings."
    }

    private fun callGemini(prompt: String, key: String): String {
        val body = JSONObject()
            .put("contents", JSONArray().put(
                JSONObject().put("parts", JSONArray().put(JSONObject().put("text", prompt)))
            )).toString().toRequestBody("application/json".toMediaType())
        val req = Request.Builder()
            .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-exp:generateContent?key=" + key)
            .post(body).build()
        return JSONObject(client.newCall(req).execute().body!!.string())
            .getJSONArray("candidates").getJSONObject(0)
            .getJSONObject("content").getJSONArray("parts")
            .getJSONObject(0).getString("text")
    }

    private fun callGroq(prompt: String, key: String): String =
        callGroqMessages(JSONArray().put(JSONObject().put("role", "user").put("content", prompt)), key)

    private fun callGroqHistory(system: String, messages: List<Pair<String, String>>, key: String): String {
        val msgs = JSONArray().put(JSONObject().put("role", "system").put("content", system))
        messages.forEach { (r, c) -> msgs.put(JSONObject().put("role", r).put("content", c)) }
        return callGroqMessages(msgs, key)
    }

    private fun callGroqMessages(messages: JSONArray, key: String): String {
        val body = JSONObject()
            .put("model", "llama-3.3-70b-versatile")
            .put("messages", messages)
            .put("max_tokens", 2000)
            .toString().toRequestBody("application/json".toMediaType())
        val req = Request.Builder()
            .url("https://api.groq.com/openai/v1/chat/completions")
            .header("Authorization", "Bearer " + key)
            .post(body).build()
        return JSONObject(client.newCall(req).execute().body!!.string())
            .getJSONArray("choices").getJSONObject(0)
            .getJSONObject("message").getString("content")
    }

    private fun callAnthropic(prompt: String, key: String): String =
        callAnthropicHistory("", listOf("user" to prompt), key)

    private fun callAnthropicHistory(system: String, messages: List<Pair<String, String>>, key: String): String {
        val msgs = JSONArray()
        messages.forEach { (r, c) -> msgs.put(JSONObject().put("role", r).put("content", c)) }
        val body = JSONObject()
            .put("model", "claude-haiku-4-5-20251001")
            .put("max_tokens", 2000)
            .put("system", system)
            .put("messages", msgs)
            .toString().toRequestBody("application/json".toMediaType())
        val req = Request.Builder()
            .url("https://api.anthropic.com/v1/messages")
            .header("x-api-key", key)
            .header("anthropic-version", "2023-06-01")
            .post(body).build()
        return JSONObject(client.newCall(req).execute().body!!.string())
            .getJSONArray("content").getJSONObject(0).getString("text")
    }

    private fun parseTasksFromResponse(response: String): List<Task> {
        return runCatching {
            val clean = response.trim()
                .removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
            val start = clean.indexOf('[')
            val end = clean.lastIndexOf(']')
            if (start == -1 || end == -1) return emptyList()
            val arr = JSONArray(clean.substring(start, end + 1))
            (0 until arr.length()).mapNotNull { i ->
                runCatching {
                    val o = arr.getJSONObject(i)
                    Task(
                        title    = o.optString("title", "Task"),
                        detail   = o.optString("detail", ""),
                        priority = runCatching { Priority.valueOf(o.optString("priority", "P2")) }.getOrDefault(Priority.P2),
                        category = runCatching { Category.valueOf(o.optString("category", "GENERAL")) }.getOrDefault(Category.GENERAL),
                        owner    = o.optString("owner", "Pradeep"),
                        deadline = o.optString("deadline", "TBD"),
                        projectGroup = o.optString("projectGroup", "General")
                    )
                }.getOrNull()
            }
        }.getOrDefault(emptyList())
    }
}
