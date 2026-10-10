package com.thelifx.app

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.text.InputType

class MainActivity : Activity() {
    private val bg = Color.rgb(10, 14, 28)
    private val panel = Color.rgb(20, 27, 47)
    private val panel2 = Color.rgb(27, 36, 61)
    private val white = Color.rgb(242, 246, 255)
    private val muted = Color.rgb(155, 169, 198)
    private val blue = Color.rgb(95, 126, 255)
    private val mint = Color.rgb(93, 230, 198)
    private lateinit var root: LinearLayout
    private lateinit var body: LinearLayout
    private var page = "Home"
    private val prefs by lazy { getSharedPreferences("the_lifx", Context.MODE_PRIVATE) }
    private val tasks = arrayListOf(
        "Plan today's priorities", "Take a short break", "Review your progress"
    )
    private val done = mutableSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        window.decorView.systemUiVisibility = 0
        showPage("Home")
    }

    private fun showPage(which: String) {
        page = which
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(bg)
            setPadding(dp(20), dp(12), dp(20), dp(8))
        }
        val scroll = ScrollView(this)
        body = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, 0, 0, dp(20))
        }
        scroll.addView(body)
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))
        root.addView(navBar())
        setContentView(root)

        when (which) {
            "Home" -> home()
            "Tasks" -> taskPage()
            "Notes" -> notesPage()
            else -> aboutPage()
        }
    }

    private fun home() {
        body.addView(label("THE LIFX  ✦", 13f, blue, true))
        body.addView(space(8))
        body.addView(label("Make today\ncount.", 34f, white, true))
        body.addView(label("Your personal space to focus, plan, and grow.", 14f, muted))
        body.addView(space(22))
        val hero = card(blue, panel2)
        hero.addView(label("YOUR DAILY RESET", 11f, Color.rgb(205, 215, 255), true))
        hero.addView(space(8))
        hero.addView(label("Small steps. Real progress.", 21f, white, true))
        hero.addView(space(8))
        hero.addView(label("Keep your goals close and your momentum going.", 13f, Color.rgb(220, 226, 255)))
        hero.addView(space(16))
        hero.addView(actionButton("Open my tasks  →", white, blue) { showPage("Tasks") })
        body.addView(hero)
        body.addView(space(20))
        body.addView(label("YOUR WORKSPACE", 12f, muted, true))
        body.addView(space(10))
        val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        row.addView(statCard("03", "Quick tasks", mint), LinearLayout.LayoutParams(0, dp(100), 1f))
        row.addView(spaceHorizontal(10))
        row.addView(statCard("∞", "Ideas & notes", blue), LinearLayout.LayoutParams(0, dp(100), 1f))
        body.addView(row)
        body.addView(space(18))
        body.addView(label("Jump back in", 19f, white, true))
        body.addView(space(10))
        body.addView(menuCard("✓", "Tasks & habits", "Organize what matters today") { showPage("Tasks") })
        body.addView(space(10))
        body.addView(menuCard("✎", "Quick notes", "Save thoughts on this device") { showPage("Notes") })
        body.addView(space(10))
        body.addView(menuCard("✦", "About the lifx", "A little space made for you") { showPage("About") })
    }

    private fun taskPage() {
        body.addView(label("YOUR PLAN", 12f, blue, true))
        body.addView(space(6))
        body.addView(label("Tasks & habits", 29f, white, true))
        body.addView(label("Check off each small win as you go.", 14f, muted))
        body.addView(space(18))
        body.addView(card().apply {
            addView(label("${done.size} of ${tasks.size} completed", 16f, white, true))
            addView(space(10))
            val progress = ProgressBar(this@MainActivity, null, android.R.attr.progressBarStyleHorizontal)
            progress.max = tasks.size
            progress.progress = done.size
            progress.progressTintList = android.content.res.ColorStateList.valueOf(mint)
            progress.progressBackgroundTintList = android.content.res.ColorStateList.valueOf(panel2)
            addView(progress, LinearLayout.LayoutParams(-1, dp(7)))
        })
        body.addView(space(12))
        tasks.forEachIndexed { i, task ->
            body.addView(card().apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                val cb = CheckBox(this@MainActivity).apply {
                    buttonTintList = android.content.res.ColorStateList.valueOf(if (done.contains(i)) mint else muted)
                    isChecked = done.contains(i)
                    setOnCheckedChangeListener { _, checked ->
                        if (checked) done.add(i) else done.remove(i)
                        showPage("Tasks")
                    }
                }
                addView(cb)
                addView(label(task, 15f, if (done.contains(i)) muted else white, done.contains(i)), LinearLayout.LayoutParams(0, -2, 1f))
            })
            body.addView(space(9))
        }
        body.addView(space(6))
        body.addView(label("Tip: make the next step small enough to start now.", 13f, muted))
    }

    private fun notesPage() {
        body.addView(label("YOUR THOUGHTS", 12f, blue, true))
        body.addView(space(6))
        body.addView(label("Quick notes", 29f, white, true))
        body.addView(label("Your notes are saved locally on this device.", 14f, muted))
        body.addView(space(18))
        val edit = EditText(this).apply {
            setText(prefs.getString("note", ""))
            hint = "Write an idea, reminder, or thought..."
            setHintTextColor(muted)
            setTextColor(white)
            textSize = 16f
            gravity = Gravity.TOP or Gravity.START
            minLines = 8
            setPadding(dp(15), dp(15), dp(15), dp(15))
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            background = rounded(panel2, 16)
        }
        body.addView(edit, LinearLayout.LayoutParams(-1, dp(220)))
        body.addView(space(12))
        body.addView(actionButton("Save note  ✓", white, blue) {
            prefs.edit().putString("note", edit.text.toString()).apply()
            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(edit.windowToken, 0)
            Toast.makeText(this, "Note saved on this device", Toast.LENGTH_SHORT).show()
            edit.clearFocus()
        })
        body.addView(space(12))
        body.addView(label("No account needed. Your note stays in this app's local storage.", 12f, muted))
    }

    private fun aboutPage() {
        body.addView(label("MADE FOR YOUR EVERYDAY", 12f, blue, true))
        body.addView(space(6))
        body.addView(label("A little more you.", 30f, white, true))
        body.addView(space(14))
        body.addView(card().apply {
            addView(label("the lifx  ✦", 22f, white, true))
            addView(space(8))
            addView(label("A personal productivity space for the little things that add up: planning, collecting ideas, and showing up for yourself.", 15f, muted))
        })
        body.addView(space(12))
        body.addView(label("WHAT'S INSIDE", 12f, muted, true))
        body.addView(space(8))
        body.addView(menuCard("✓", "Daily tasks", "A simple checklist with progress") {})
        body.addView(space(8))
        body.addView(menuCard("✎", "Private notes", "Stored locally on your phone") {})
        body.addView(space(8))
        body.addView(menuCard("◈", "Minimal design", "A calm dark interface with blue accents") {})
        body.addView(space(18))
        body.addView(label("Version 1.0  •  Built for your next chapter.", 12f, muted))
    }

    private fun navBar(): View {
        val bar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setPadding(dp(4), dp(7), dp(4), dp(7))
            background = rounded(panel, 20)
        }
        listOf("Home", "Tasks", "Notes", "About").forEach { item ->
            val selected = page == item
            val col = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(dp(5), dp(8), dp(5), dp(8))
                background = if (selected) rounded(panel2, 14) else null
                setOnClickListener { showPage(item) }
            }
            col.addView(label(
                when(item) { "Home" -> "⌂"; "Tasks" -> "✓"; "Notes" -> "✎"; else -> "✦" },
                19f, if (selected) mint else muted, true
            ).apply { gravity = Gravity.CENTER })
            col.addView(label(item, 10f, if (selected) white else muted, selected).apply { gravity = Gravity.CENTER })
            bar.addView(col, LinearLayout.LayoutParams(0, -2, 1f))
        }
        return bar
    }

    private fun menuCard(icon: String, title: String, subtitle: String, click: () -> Unit): View {
        return card().apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            addView(label(icon, 23f, mint, true).apply { gravity = Gravity.CENTER }, LinearLayout.LayoutParams(dp(42), dp(42)))
            addView(spaceHorizontal(10))
            val texts = LinearLayout(this@MainActivity).apply { orientation = LinearLayout.VERTICAL }
            texts.addView(label(title, 15f, white, true))
            texts.addView(space(4))
            texts.addView(label(subtitle, 12f, muted))
            addView(texts, LinearLayout.LayoutParams(0, -2, 1f))
            addView(label("›", 24f, muted))
            setOnClickListener { click() }
        }
    }

    private fun statCard(value: String, title: String, color: Int): View = card().apply {
        addView(label(value, 26f, color, true))
        addView(space(5))
        addView(label(title, 12f, muted))
    }

    private fun card(color: Int? = null, unused: Int? = null): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(16), dp(16), dp(16))
            background = rounded(color ?: panel, 18)
        }
    }

    private fun actionButton(text: String, fg: Int, fill: Int, click: () -> Unit): View {
        return TextView(this).apply {
            this.text = text
            textSize = 15f
            setTextColor(fg)
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
            setPadding(dp(16), dp(14), dp(16), dp(14))
            background = rounded(fill, 14)
            setOnClickListener { click() }
        }
    }

    private fun label(text: String, size: Float, color: Int, bold: Boolean = false): TextView =
        TextView(this).apply {
            this.text = text
            textSize = size
            setTextColor(color)
            if (bold) typeface = Typeface.DEFAULT_BOLD
            setLineSpacing(dp(2).toFloat(), 1.0f)
        }

    private fun rounded(color: Int, radius: Int): GradientDrawable =
        GradientDrawable().apply { setColor(color); cornerRadius = dp(radius).toFloat() }

    private fun space(h: Int): View = View(this).apply { layoutParams = LinearLayout.LayoutParams(1, dp(h)) }
    private fun spaceHorizontal(w: Int): View = View(this).apply { layoutParams = LinearLayout.LayoutParams(dp(w), 1) }
    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
