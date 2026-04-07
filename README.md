# Kavvach (कवच - The Shield)
### AI Voice Clone Detector: Defending the Digital Citizen from GenAI Scams

**Kavvach** is a proactive security tool designed to protect vulnerable users from the rising threat of GenAI-powered voice cloning scams. In an era where AI can mimic a loved one's voice with 99% accuracy, Kavvach acts as a digital shield by verifying the authenticity of a caller's voice in real-time.

---

## 🛡️ Proactive Scam Prevention
The primary goal of Kavvach is to **Defend the Digital Citizen**. By providing a simple, reliable way to distinguish between a real human and an AI-generated voice, we:
- **Prevent Financial Scams**: Stop "Emergency" calls where scammers pose as family members.
- **Educate Vulnerable Users**: Give citizens the tools to question digital interactions.
- **Rebuild Trust**: Enable secure digital communication in an AI-driven world.

## 🚀 Key Features
- **100% Offline Performance**: Data never leaves the device. Complete privacy and trust.
- **Zero-Latency Analysis**: Uses ONNX Runtime for high-performance inference.
- **Explainable Results**: Provides a confidence score alongside the prediction.
- **Material 3 Design**: A modern, clean interface designed for accessibility.

---

## 🛠️ Architecture & How It Works

### The Workflow
1. **Audio Capture**: The app records 5 seconds of audio using the Android `AudioRecord` API (16kHz, Mono, 16-bit PCM).
2. **Preprocessing**: Raw PCM data is converted into a normalized float array (-1.0 to 1.0).
3. **Local Inference**: The pre-trained Deepfake Detection model (`.onnx`) processes the audio buffer locally via **ONNX Runtime**.
4. **Scam Verification**: The model outputs a probability score. Scores above 60% trigger an "AI Voice Clone Detected" alert.

### Tech Stack
- **Kotlin**: Core application logic.
- **Jetpack Compose**: Modern declarative UI.
- **ONNX Runtime Mobile**: High-performance AI engine for Android.
- **Kotlin Coroutines**: Non-blocking background inference for a smooth UI experience.

---

## 🏗️ Project Structure
```text
ai/
  OnnxModelRunner.kt  - Handles model loading and inference session.
audio/
  AudioRecorder.kt    - Manages microphone input and buffer capture.
ui/
  MainActivity.kt     - Coordinates UI state and permissions.
  ResultView.kt       - Renders detection results and confidence cards.
utils/
  AudioUtils.kt       - Provides audio data normalization utilities.
```

## 📦 Requirements
- Minimum SDK: 24 (Android 7.0+)
- Permissions: `RECORD_AUDIO`
- Asset: `voice_detector.onnx` 

> [!IMPORTANT]
> Due to file size limits, the `voice_detector.onnx` model is not included in this repository. 
> You must manually place your model file in: `app/src/main/assets/voice_detector.onnx`

---

## 📜 Problem Statement: Defend the Digital Citizen
Built for the hackathon challenge to create proactive GenAI tools that prevent scams and rebuild trust. Kavvach is our answer to the voice-phishing (vishing) epidemic.
**WORKING DEMO VIDEO OF KAVVACH -->** https://youtu.be/lsttYb5XJXs
Developed by **Sagar Gidwani**.
