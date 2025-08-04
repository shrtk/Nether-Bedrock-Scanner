# Nether Bedrock Scanner (Fabric 1.21.8)

A lightweight Fabric mod that scans bedrock positions in your current chunk and its surrounding 8 chunks, specifically at Y=4 and Y=123.  
Designed for integration with [Nether_Bedrock_Cracker](https://github.com/19MisterX98/Nether_Bedrock_Cracker), this mod outputs results in a simple plain-text format for external analysis.

## 🔧 Features

- `/bedrockscan` command:
  - Scans a **3x3 chunk area** centered on the player
  - Targets **Y=4** and **Y=123**
  - Detects only **bedrock blocks**
  - Outputs lines like:  
    ```
    111 4 111 Bedrock
    222 123 222 Bedrock
    ```

- Results are saved to:  
  - `~/Downloads/bedrockscan_YYYYMMDD_HHmmss`  
  - This makes it easy to import into external tools or open directly from file managers on **Windows, macOS, and Linux**

## 🧠 Motivation

This mod exists as a companion utility for [Nether_Bedrock_Cracker](https://github.com/19MisterX98/Nether_Bedrock_Cracker), enabling players to:
- Quickly and consistently extract bedrock layouts from the Nether
- Improve reproducibility and automation for seed cracking or terrain analysis

## 📝 License

MIT License
