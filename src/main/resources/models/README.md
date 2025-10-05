# OpenNLP Models Directory

This directory should contain the Apache OpenNLP pre-trained models for Natural Language Processing.

## Required Models

Download the following models from: https://opennlp.sourceforge.net/models-1.5/

1. **en-token.bin** - English Tokenizer Model

   - Direct download: http://opennlp.sourceforge.net/models-1.5/en-token.bin

2. **en-ner-person.bin** - Person Name Recognition Model
   - Direct download: http://opennlp.sourceforge.net/models-1.5/en-ner-person.bin

## Installation Instructions

1. Download both `.bin` files from the links above
2. Place them directly in this `models` directory
3. Rebuild the project with `mvn clean install`
4. The application will automatically load these models on startup

## Optional Models (for enhanced features)

You may also download these additional models for more NLP capabilities:

- **en-ner-location.bin** - Location recognition
- **en-ner-organization.bin** - Organization recognition
- **en-ner-date.bin** - Date recognition

## Directory Structure

After downloading, your structure should look like:

```
models/
├── en-token.bin
├── en-ner-person.bin
└── README.md (this file)
```

## Fallback Behavior

If models are not found, the application will:

- Use basic string tokenization instead of the tokenizer model
- Use regex patterns for name extraction instead of NER model
- Display a warning message in the console
- Continue to function with reduced accuracy

## Model Information

- **License**: Apache License 2.0
- **Source**: Apache OpenNLP Project
- **Version**: 1.5 (compatible with OpenNLP 1.9.x)
- **Language**: English

## Troubleshooting

### Models Not Loading

If you see "Warning: Could not load NLP models" in the console:

1. Verify files are in the correct directory: `src/main/resources/models/`
2. Check file names are exactly: `en-token.bin` and `en-ner-person.bin`
3. Ensure files are not corrupted (re-download if necessary)
4. Rebuild the project after adding models

### File Permissions

On Linux/Mac, ensure the model files have read permissions:

```bash
chmod 644 src/main/resources/models/*.bin
```

## More Information

- Apache OpenNLP: https://opennlp.apache.org/
- Model Documentation: https://opennlp.apache.org/models.html
- Model Training: https://opennlp.apache.org/docs/
