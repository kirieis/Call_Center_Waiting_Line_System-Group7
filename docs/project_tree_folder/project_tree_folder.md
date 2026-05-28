project-root/
├── src/
│   └── callcenter/
│       ├── Main.java
│       │
│       ├── model/
│       │   ├── Call.java
│       │   └── CallStatus.java
│       │
│       ├── core/
│       │   ├── queue/
│       │   │   ├── StandardQueue.java
│       │   │   └── PriorityCallQueue.java
│       │   │
│       │   └── routing/
│       │       ├── CallRouter.java
│       │       └── AgingAlgorithm.java
│       │
│       ├── ui/
│       │   ├── MainMenu.java
│       │   ├── InputHandler.java
│       │   ├── ConsoleRenderer.java
│       │   └── StateManager.java
│       │
│       ├── storage/
│       │   ├── FileHandler.java
│       │   └── CallHistoryStore.java
│       │
│       └── experiment/
│           ├── Exp1_PriorityQueue.java
│           ├── Exp2_AgingAlgorithm.java
│           ├── Exp3_HistoryLookup.java
│           └── Exp4_ConsoleRender.java
│
├── test/
│   ├── blackbox/
│   │   ├── InputValidationTest.java
│   │   └── MenuNavigationTest.java
│   │
│   └── whitebox/
│       ├── AgingAlgorithmTest.java
│       └── MaxHeapTest.java
│
├── data/
│   └── call_history.csv
│
├── docs/
│   ├── AI_logs/
│   ├── diagrams/
│   │   ├── use_case_diagram.png
│   │   └── class_diagram.png
│   │
│   └── experiment_results/
│       ├── exp1_priority_queue.xlsx
│       ├── exp2_aging_algorithm.xlsx
│       ├── exp3_history_lookup.xlsx
│       └── exp4_console_render.xlsx
│
└── README.md